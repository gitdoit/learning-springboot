package org.seefly.nio.selector;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liujianxin
 * @date 2019-01-08 14:08
 */
public class SelectorDemo {

    @Test
    public void test() throws IOException {
        Selector open = Selector.open();
        Selector opena = Selector.open();

        System.out.println(open == opena);
    }


    /**
     * 将一个Channel注册到Selector上
     * 并告诉Selector你对这个通道上的那些事件感兴趣，需要注意的是这个Channel需要是非阻塞的。
     * 这样一来，就不必单开一个线程以阻塞的方式等待你感兴趣的事件到来。
     */
    public void registerInfo() throws IOException {
        // 创建一个selector
        // TODO 全局调用这个静态方法 是否返回相同的Selector？
        // 返回不同的
        Selector open = Selector.open();
        // 创建一个tcp客户端通道
        SocketChannel socketChannel = SocketChannel.open();
        // 注意，如果channel和selector一起工作时，channel不能处于阻塞模式，这意味着FileChannel无法使用Selector
        socketChannel.configureBlocking(false);
        // 将客户端通道注册到selector，第二个参数告诉selector你监听这个channel时对它的哪个事件感兴趣
        // 有四个常量代表不同的事件：读就绪：表示有一个通道有可读的数据，写就绪：代表有一个通道可以写数据
        // 连接就绪：表示有一个channel成功连接到某个服务器，接受就绪：一个server socket channel准备好接收新进入的连接
        // 如果对某个channel上的几个事件感兴趣，则使用 SelectionKey.OP_READ | SelectionKey.OP_WRITE 作为注册参数
        // TODO 将一个Channel注册到多个Selector上会怎样？
        SelectionKey selectionKey = socketChannel.register(open, SelectionKey.OP_READ);
    }

    /**
     * 上面注册之后的返回值：SelectionKey，它包含了四个信息
     * interest集合：即我们注册时，传入的感兴趣的事件集合
     * ready集合
     * Channel
     * Selector
     */
    public void selectionKeyInfo(SelectionKey selectionKey) {
        // 位与操作可以判断出来，你注册的感兴趣事件
        int interestSet = selectionKey.interestOps();
        boolean isInterestedInAccept = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT;
        boolean isInterestedInConnect = (interestSet & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT;
        boolean isInterestedInRead = (interestSet & SelectionKey.OP_READ) == SelectionKey.OP_READ;
        boolean isInterestedInWrite = (interestSet & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE;

        // 检测通道中哪些事件已经就绪
        selectionKey.isAcceptable();
        selectionKey.isConnectable();
        selectionKey.isReadable();
        selectionKey.isWritable();

        // 通过selectionKey 获取Channel 和 Selector
        Channel channel = selectionKey.channel();
        Selector selector = selectionKey.selector();

        // 给通道附加附件，这样可以方便的识别出指定的通道，例如附加Buffer
        Object idInfo = new Object();
        selectionKey.attach(idInfo);
        Object attachedObj = selectionKey.attachment();
    }

    /**
     * selector.select()
     * 有三个重载方法，这些方法返回你所感兴趣的事件已经就绪的通道
     * 如连接、接受、读、写已经就绪的通道。 这三个方法的返回值int，表示已经有多少个通道就绪。
     * 亦即，自上次调用select()方法后有多少通道变成就绪状态。
     * <p>
     * selector.selectedKeys()
     * 一旦select()方法表明有一个或多个通道就绪之后，可以调用此方法。
     * 问题：返回值是这个Selector中所有注册的Channel还是准备好的那些？？
     * 是已经准备好的那些
     */
    public void selectInfo(Selector selector) throws IOException {
        // 阻塞到至少有一个通道在你注册的事件上就绪了。
        int select = selector.select();
        // 具有超时时间的阻塞
        int select1 = selector.select(300);
        // 不阻塞
        int i = selector.selectNow();


        // 调用select()方法阻塞完毕之后返回值表明有一个或多个通道就绪，然后可以调用下面这个方法
        // 它返回发生了事件的 SelectionKey 对象的一个 集合
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        // 我们可以遍历这个集合，来访问已经就绪的通道;
        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            // 下面的代码确定发生的是什么IO事件，并执行对应的逻辑
            if (key.isAcceptable()) {
                // 需要自己转换成实际需要处理的Chanel类型
                key.channel();
            } else if (key.isConnectable()) {
                // a connection was established with a remote server.
            } else if (key.isReadable()) {
                // a channel is ready for reading
            } else if (key.isWritable()) {
                // a channel is ready for writing
            }
            // Selector不会自己从已选择键集中移除SelectionKey实例
            // 不许自己移除，下次该通道再就绪时，会被再次放入。
            keyIterator.remove();
        }
    }

    /**
     * 由于selectorA.select()方法会阻塞线程，所以需要有一种方式
     * 将被阻塞的线程唤醒，这个时候可以在其它线程中调用selectorA.wakeUp()。
     * 注意，两个线程操作的Selector要是同一个。
     * <p>
     * 另外，如果没有线程由于调用select方法被阻塞，那么此时调用唤醒方法
     * 那么下一个调用select方法的线程会立即返回。
     */
    public void wakeUp(Selector selector) throws IOException {
        // 这里模拟线程A，调用select方法被阻塞了
        {
            selector.select();
        }

        // 这里模拟线程B，调用wakeUp方法，唤醒线程A
        {
            selector.wakeup();
        }
    }

    /**
     * 用完Selector后调用其close()方法会关闭该Selector，
     * 且使注册到该Selector上的所有SelectionKey实例无效。通道本身并不会关闭。
     */
    public void close(Selector selector) throws IOException {
        selector.close();
    }
}
