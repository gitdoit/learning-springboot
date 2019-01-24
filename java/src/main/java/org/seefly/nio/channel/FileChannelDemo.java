package org.seefly.nio.channel;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * java NIO Channel通道和流非常相似，区别如下
 * 1.通道可以读也可以写，流一般来说是单向的（只能读或者写，所以之前我们用流进行IO操作的时候需要分别创建一个输入流和一个输出流）。
 * 2.通道可以异步读写。
 * 3.通道总是基于缓冲区Buffer来读写
 * 主要的几个管道实现
 * FileChannel： 用于文件的数据读写
 * DatagramChannel： 用于UDP的数据读写
 * SocketChannel： 用于TCP的数据读写，一般是客户端实现
 * ServerSocketChannel: 允许我们监听TCP链接请求，每个请求会创建会一个SocketChannel，一般是服务器实现
 *
 * @author liujianxin
 * @date 2019-01-07 16:45
 */
public class FileChannelDemo {

    /**
     * 从管道中读取数据
     * 和原来的IO相比，
     */
    @Test
    public void testRead() throws IOException, InterruptedException {
        RandomAccessFile aFile = new RandomAccessFile("E:\\test\\nio.txt", "rw");
        FileChannel channel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(64);
        int read = channel.read(buf);
        Thread.sleep(20000);
        // 关闭通道
        channel.close();
        buf.flip();
        System.out.println(new String(buf.array(), StandardCharsets.UTF_8));
    }








    /**
     * 测试向管道流中写入数据
     */
    @Test
    public void testWrite() throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("E:\\test\\nio.txt", "rw");
        FileChannel channel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.put("hello world!".getBytes());
        // 反转一下
        buf.flip();
        System.out.println("before write:"+buf);
        channel.write(buf);
        System.out.println("after write:"+buf);
        channel.close();
    }

    /**
     * 利用一同一个管道进行读和写
     */
    @Test
    public void testReadAndWrite() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("E:\\test\\nio.txt", "rw");
        //通过RandomAccessFile对象的getChannel()方法。FileChannel是抽象类。
        FileChannel channel = raf.getChannel();
        // 创建缓存区
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        // 读入数据到缓存
        int read = channel.read(readBuffer);
        // 创建写缓存
        ByteBuffer writeBuffer = ByteBuffer.allocate(48);
        writeBuffer.put("hello channelddd!".getBytes());
        // 切换读写模式，限制=位置 位置=0;注意这里，实际上就是改变了一下这几个参数，其他没有任何变化，就是逻辑上的切换。
        writeBuffer.flip();
        channel.write(writeBuffer);
        raf.close();

    }


    /**
     * 将一个管道中读取的数据分散到不同的缓存中
     * 还有一个反向操作的不演示了
     */
    @Test
    public void testScatter() throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("E:\\test\\nio.txt", "rw");
        FileChannel channel = aFile.getChannel();
        ByteBuffer a = ByteBuffer.allocate(6);
        ByteBuffer b = ByteBuffer.allocate(6);
        ByteBuffer[] array = {a,b};
        // 文件内容12个字节[hello world!]，这里分散成俩
        channel.read(array);
        b.flip();
        // 读后面的
        while (b.hasRemaining()){
            System.out.print((char)b.get());
        }
    }


    /**
     * 俩管道桥接一下
     */
    @Test
    public void testTransfer() throws IOException {
        RandomAccessFile sourceFile = new RandomAccessFile("E:\\test\\nio.txt", "rw");
        RandomAccessFile targetFile = new RandomAccessFile("E:\\test\\to.txt", "rw");
        FileChannel sourceFileChannel = sourceFile.getChannel();
        FileChannel targetFileChannel = targetFile.getChannel();
        sourceFileChannel.transferTo(0,12,targetFileChannel);
        sourceFile.close();
        targetFile.close();
    }


}
