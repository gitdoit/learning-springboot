package org.seefly.nio.buffer;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Java NIO Buffers用于和NIO Channel交互。 我们从Channel中读取数据到buffers里，从Buffer把数据写入到Channels.
 * Buffer本质上就是一块内存区，可以用来写入数据，并在稍后读取出来。这块内存被NIO Buffer包裹起来，对外提供一系列的读写方便开发的接口。
 * java的核心缓冲区有7个基本类型对应的缓冲区。
 *
 * 一个缓冲区即可以读，也可以写。但读写不能同时进行，所以需要进行模式切换。
 * 步骤如下：
 * 1.把数据写入缓存区
 * 2.调用flip，切换为读模式
 * 3.从缓存中读取数据
 * 4.调用clear/compact清除已读数据
 *
 * 对于Buffer有三个属性需要知道
 * 1.capacity 容量
 * 2.position 位置
 * 3.limit 上限
 * 对于容量，在读/写模式下都表示同一个东西，就是这个缓存区的大小。
 * 对于位置，在读模式下表示的是你要从哪开始读，默认初始化0，对于写模式，则为你要从哪开始写。
 * 对于上线，在读模式下表示你可以读的最大数据量，对于写模式表示你能够写的最大数据量。
 *
 *
 *
 * https://mp.weixin.qq.com/s?__biz=MzU4NDQ4MzU5OA==&mid=2247483961&idx=1&sn=f67bef4c279e78043ff649b6b03fdcbc&chksm=fd985458caefdd4e3317ccbdb2d0a5a70a5024d3255eebf38183919ed9c25ade536017c0a6ba#rd
 * @author liujianxin
 * @date 2019-01-07 11:00
 */
public class ByteBufferDemo {
    /**分配缓冲区,position=0 capacity=48 limit=capacity*/
    private ByteBuffer byteBuffer = ByteBuffer.allocate(48);


    /**
     * 测试重置方法
     * 重置方法要和标记方法配合使用，不然会抛出异常。
     * 感觉他就相当于一个还原点，例如当前读取到position=10的时候，mark一下
     * 继续操作到position=55，这时候使用reset方法，则会还原到position=10
     */
    @Test
    public void testRest(){
        // set position=0 capacity=33 limit=capacity
        // 这个方法并不会真的清空缓存，只是将这三个标志位重置
        byteBuffer.clear();
        //设置位置
        byteBuffer.position(5);
        //标记  还原点 5
        byteBuffer.mark();
        byteBuffer.position(10);
        System.out.println("before reset:"+byteBuffer);
        // 还原到 5
        byteBuffer.reset();
        System.out.println("after reset:"+byteBuffer);
    }

    /**
     * 测试rewind方法，源码就是position=0 mark=-1
     * 就是用来再读一边，或者重头开始写一边
     */
    @Test
    public void testRewind(){
        byteBuffer.position(15);
        // 看源码
        byteBuffer.limit(10);
        System.out.println("before rewind " + byteBuffer);
        // position=0 mark=-1
        byteBuffer.rewind();
        System.out.println("after rewind "+ byteBuffer);
    }

    /**
     * 直观的感觉就是，在读缓存的时候没有读完，这时候去干其他的事情
     * 但是回来的时候还想从这个地方继续读下去，那就在走之前使用这个方法，回来的
     * 时候调用一下flip继续读下去。
     * 虽然这个方法直译过来叫做紧凑，但是它并没有删除任何元素。
     *
     * 这个方法将没有读完剩下的那些元素移动[0,limit-position]区间内，
     * position=limit-position
     * limit=capacity
     */
    @Test
    public void testCompact(){
        // position="abcd".getBytes().length
        byteBuffer.put("abcd".getBytes());
        System.out.println("before compact" + byteBuffer);
        System.out.println("content:"+new String(byteBuffer.array()));
        // 读写交换 limit = position; position = 0; mark = -1
        byteBuffer.flip();
        System.out.println("after flip  " + byteBuffer);
        // position++
        System.out.print((char) byteBuffer.get());
        System.out.print((char) byteBuffer.get());
        System.out.println((char) byteBuffer.get());
        //读三个  留一个
        System.out.println("after get " + byteBuffer);
        System.out.println("context:"+new String(byteBuffer.array()));
        byteBuffer.compact();
        System.out.println("after compact"+byteBuffer);
        System.out.println("context:"+new String(byteBuffer.array()));

    }

    @Test
    public void testPut(){
        byteBuffer.put((byte)'s');
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.put((byte)'a');
        allocate.flip();
        // 这里对allocate也有影响的，相当于读缓存
        byteBuffer.put(allocate);
        System.out.println(byteBuffer);
        System.out.println(allocate);
    }
}
