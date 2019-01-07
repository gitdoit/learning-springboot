package org.seefly.nio.channel;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * nio替代原来的阻塞的套接字编程
 * DatagramChannel 基于UDP
 * SocketChannel 基于TCP
 * @author liujianxin
 * @date 2019-01-07 19:31
 */
public class SocketChannelDemo {

    /**
     * 模拟客户端
     */
    @Test
    public void client() throws IOException {
        // 拿到一个SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        // 连接到远程服务器
        socketChannel.connect(new InetSocketAddress("127.0.0.1",6666));
        // 创建一个缓冲区
        ByteBuffer cache = ByteBuffer.allocate(128);
        cache.put("hello WebServer this is from WebClient".getBytes());
        cache.flip();
        // 写入管道
        socketChannel.write(cache);
        // 从管道接受数据
        cache.flip();
        socketChannel.read(cache);
        // 打印缓冲中接收到的数据
        cache.flip();
        StringBuilder sb = new StringBuilder();
        while (cache.hasRemaining()){
            sb.append((char) cache.get());
        }
        System.out.println("message from server:"+sb.toString());
        socketChannel.close();
    }

    @Test
    public void testServer() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 为什么还要绑定地址？
        ssc.socket().bind(new InetSocketAddress("127.0.0.1",6666));
        // 获取一个套接字管道
        SocketChannel socketChannel = ssc.accept();
        // 创建缓存
        ByteBuffer cache = ByteBuffer.allocate(128);
        cache.put("hello WebClient this is from WebServer".getBytes());
        cache.flip();
        // 写入管道
        socketChannel.write(cache);
        StringBuilder sb = new StringBuilder();
        cache.flip();
        socketChannel.read(cache);
        cache.flip();
        while (cache.hasRemaining()){
            sb.append((char) cache.get());
        }
        System.out.println("message from client:"+sb.toString());

    }
}
