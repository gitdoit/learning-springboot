package org.seefly.nio.channel;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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

    public static void main(String[] args) throws IOException {
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

        /*while (read != -1){
            System.out.println("read  "+read);
            readBuffer.flip();
            while (readBuffer.hasRemaining()){
                System.out.print((char) readBuffer.get());
            }
            readBuffer.clear();
            read = channel.read(readBuffer);
        }*/
        raf.close();
    }


    /**
     * 测试从管道流中读取数据
     */
    @Test
    public void testRead() throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("E:\\test\\nio.txt", "rw");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(9);
        // 从管道中读取数据放入缓存，返回读取的字节数。-1为已读取到文件末尾
        int bytesRead = inChannel.read(buf);
        // 判断是否读取到文件末尾
        while (bytesRead != -1) {
            // 打印读取的字节数
            System.out.println("Read " + bytesRead);
            // 从写模式转换到读模式，limit 交换  position , position=0
            buf.flip();
            // position < limit ?
            while(buf.hasRemaining()){
                // position++
                System.out.print((char) buf.get());
            }
            System.out.println();
            // position = 0 ,limit = capacity
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }
}
