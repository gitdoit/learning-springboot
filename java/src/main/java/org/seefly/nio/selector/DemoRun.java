package org.seefly.nio.selector;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liujianxin
 * @date 2019-01-08 17:07
 */
public class DemoRun {

    @Test
    public void server() throws IOException {
        // 准备好一个篮子，用来放通道接收或者需要发送的数据
        ByteBuffer lanzi = ByteBuffer.allocate(128);
        // 标准回答
        ByteBuffer reply = ByteBuffer.allocate(128);
        reply.put("ni hao sao a\r\n".getBytes());
        reply.flip();


        // 开启一个服务
        ServerSocketChannel server = ServerSocketChannel.open();
        // 监听指定端口，非阻塞才能用Selector
        server.bind(new InetSocketAddress("127.0.0.1",6666)).configureBlocking(false);
        // 来一个Selector
        Selector selector = Selector.open();
        // 注册,服务器对accept感兴趣
        server.register(selector, SelectionKey.OP_ACCEPT);
        // 当前线程用来循环派发事件
        while (true){
            // 不需要返回值吧，这个方法阻塞的
            selector.select();
            // 阻塞完毕，说明有感兴趣的事件到来
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 遍历这些事件，找到他们的处理方法
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                // 是不是一个新连接需要建立呀
                if(key.isAcceptable()){
                    // 就注册了一个服务端套接字管道，强转过来
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    // 这里不会阻塞了，因为是事件触发
                    SocketChannel accept = channel.accept();
                    // 不能阻塞，因为要注册到Selector里面，等客户端准可读
                    accept.configureBlocking(false);
                    // 连接已建立，注册一个可读事件
                    accept.register(selector,SelectionKey.OP_READ);
                }
                // 是不是客户端往服务器写数据了呀
                else if(key.isReadable()){
                    lanzi.clear();
                    SocketChannel channel = (SocketChannel)key.channel();
                    channel.read(lanzi);
                    lanzi.flip();
                    System.out.println("客户端:"+new String(lanzi.array()));
                    // 服务器有话要说
                    key.interestOps(SelectionKey.OP_WRITE);
                }
                // 是不是客户端等服务器写数据了呀
                else if(key.isWritable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    channel.write(reply);
                    reply.rewind();
                    // 服务器说完了，想听客户端说话
                    key.interestOps(SelectionKey.OP_READ);
                }
                iterator.remove();
            }
        }
    }


    /**
     * 客户端，就用原始的了
     */
    @Test
    public void client() throws IOException {
        Socket client = new Socket();
        client.connect(new InetSocketAddress("127.0.0.1", 6666));
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter writer = new PrintWriter(client.getOutputStream(), true)) {
            while (true){
                writer.println("hahahaha");
                Thread.sleep(1000);
                String s = reader.readLine();
                System.out.println(s);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
