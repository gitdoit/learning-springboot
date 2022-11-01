# 一 用户空间和内核空间
 为了避免用户应用导致冲突甚至内核崩溃，用户应用与内核是分离的；
 1. 进程的寻址空间会划分为两部分： 内核空间、用户空间

 Linux系统为了提高IO小磊，会在用户空间和内核空间都加入缓冲区：
 1. 写数据时，要把用户缓冲数据拷贝到内核缓冲区，然后写入设备
 2. 读数据时，要从设备读取数据到内核缓冲区，然后拷贝到用户缓冲区

# 阻塞IO
读取数据时，如果没有数据可读，会一直阻塞知道数据可读

# 非阻塞IO
读取数据时，如果没有数据可读，会直接返回，不会阻塞等待。
但是这时候一般都会轮询读取，一直没有数据会造成CPU空转。

# IO多路复用
 无论是阻塞IO还是非阻塞IO，用户在一阶段都需要调用recvfrom来获取数据
区别在于无数据时的处理方案
1. 如果调用recvfrom时，恰好没有数据，阻塞IO会时调用线程阻塞，非阻塞IO使CPU空转轮询等待
2. 如果调用recvfrom时，恰好有数据，则用户进程可以直接进入二阶段，并读取处理数据


比如服务端处理客户端socket请求时，在单线程情况下，只能一次处理每一个socket，如果正在处理的socket没有就绪
线程就会被阻塞，同时其他所有客户端的socket都必须等待。

**文件描述符** : 简称FD，是一个从0开始递增的无符号整数，用来关联Linux中的一个文件。
在Linux中，一切皆文件，例如常规文件，视频，硬件设备等。当然也包括网络套接字。
**IO多路复用**： 是利用单个线程同时监听多个FD,并且在某个FD可读，可写的时候得到通知，从而
避免无效的等待，充分利用CPU资源。

### IO多路复用-select
```
select(...fds) --> any of fds got ready --> loop all fds --> recvfrom
```
利用一个1024位的`fd_set`（结构是一个bitmap），在用户态和内核态之间
来回传递，当某个fd就绪的时候内核系统会将指定的fd位置值为1，同时传递
给用户程序，此时，用户程序只能知道有fd就绪了，但是不知道是哪个，所以要进行
一次遍历，找到这个就绪的fd。
同时调用select函数会触发阻塞，直到有fd就绪

### IO多路复用-poll
```
poll(...fds) --> any of fds got ready --> loop all fds --> recvfrom
```
为了弥补select方法只能传递1024个fd的缺陷，poll对这部分做了提升，可以传递
一个pollfd{int:fd,int:events,int:revents}数组，避免了长度限制，但是
还是没有避免这个数组在用户态和内核态之间来回拷贝以及遍历所有数组的缺陷。提升不大

### IO多路复用-epoll
```
epoll(...fds) ---> ready fds --> recvfrom 

struct eventpoll {
 struct rb_root rbt; // 一颗红黑树
 struct list_head rdlist;// 一个连表， 记录就绪的fd
 // ...
}
// 1 会在内核创建eventpoll结构体，返回对应的句柄epfd
int epoll_create(int size)

// 2 将一个fd添加到epoll的红黑树中，并设置ep_poll_callback
// 当回调触发时，九八对应的fd加入到rdlist这个就绪队列
int epoll_ctl(
  int epfd,// 要操作的epoll实例
  int op,// 执行的操作，增删改
  int fd,//要监听的fd
  struct epoll_event * event
)

// 3 检查rdlist是否为空，不空则返回就绪的fd数量
int epoll_wait(
  int epfd,// 要操作的epoll实例
  struct epoll_event *events,// 空的event数组，用于接受就绪fd
  int maxevents,// events数组最大长度
  int timeout// 超时时间
)
```
通过epoll_create创建一颗红黑树和连表，每次将需要监听的fd添加到这个
红黑树中。调用epoll_wait方法时，当内核发现某个fd就绪了会将他添加到就绪链表中
所以，只有可用的fd才会被返回给用户程序。
这种做法避免了select和poll方法的fd来回复制，同时也避免了遍历整个fd数组。效率提升很高

### IO多路复用总结
**select模式存在的问题**
1. 能监听fd的最大不超过1024个
2. 每次select都需要把要监听的fd拷贝到内核空间
3. 每次都要遍历所有fd来判断就绪状态

**poll模式存在的问题**
1. poll利用了连表解决了select模式的fd长度限制，但是仍然需要遍历所有fd

**epoll是如何解决这些问题的**
1. 基于epoll实例中的红黑树，保存需要监听的fd，理论上无上限，而且增删改效查效率都很高
   性能不会随着fd数量增多而下降
2. 每个fd只需要执行一次epoll_ctl添加到红黑树中，以后每次epoll_wait不再需要传递
   这些fd，只需要传递epfd(epoll实例)就行
3. 内核会将就绪的fd直接拷贝到用户空间的指定位置，用户进程无需遍历所有fd就能知道就绪
   的fd是哪个

### IO多路复用-事件通知机制
当FD有数据可读是，我们调用epoll_wait就可以得到通知，但是事件通知的模式有两种
1. LevelTriggered：简称LT
   当fd有数据可读时，会重复通知多次，直到数据处理完成。时epoll的默认模式
2. EdgeTriggered：简称ET
   当fd有数据可读时，只会被通知一次，不管数据是否处理完成
例如：
   1. 假设客户端socket对应的fd已经注册到epoll实例中
   2. 客户端socket发送了2kb数据
   3. 服务端调用epoll_wait，得到就绪的fd
   4. 服务端从fd读取了1kb数据
   5. 回到步骤3，再次循环


## Redis网络模型

















