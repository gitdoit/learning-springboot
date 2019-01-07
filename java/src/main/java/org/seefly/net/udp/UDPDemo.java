package org.seefly.net.udp;

import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 2017年7月24日
 * UDP发送：
 *  把数据打包
 *  数据有限制:每一个包的size都在64k之内，因为如果包的size超过64k，在IP层组包会发生错误，包会被丢弃。接收端不能组包，从而会将整个IP数据报丢弃
 *  不建立连接
 *  速度快
 *  不可靠
 *  Socket在用于UDP的发送时，不需要指定端口或IP地址
 *  IP地址以及端口号需要在建包时被指定。
 *  建包时需要指定发送的数据，数据的真实长度，指定发送地址，以及端口号
 *
 * UPD接受：
 * 步骤：
 *  1，创建接受端Socket对象，指定接收端口
 *  2，创建数据包用来存储接收的数据
 *  3，socket方法用来接收数据
 *  4，解析并输出
 *  5，关闭资源
 *
 *  UDP中的socket在用于接收数据时要指定从哪个端口接收数据。
 *  而容器数据包，即接收数据包只要指定字节数组，接收数据的最大长度即可。
 *  可以通过调用数据包的getLength方法获取接收数据的真实长度。
 *  通过调用getData获取数据的字节数组。
 * @author liujianxin
 */
public class UDPDemo {

    @Test
    public void testSend()throws IOException {
        //1.新建发送方UDP的Socket对象
        DatagramSocket sen = new DatagramSocket();
        //2.创建数据
        byte[] mes = "Hello UDP!".getBytes();
        int len = mes.length;
        InetAddress ip = InetAddress.getByName("127.0.0.1");
        //端口，随便定。可能冲突
        int port = 10086;
        //3.包装数据，包含数据，长度，地址，端口
        DatagramPacket dp = new DatagramPacket(mes, len, ip, port);
        //4.发送数据
        sen.send(dp);
        //关闭资源
        sen.close();
    }

    @Test
    public void testReceive() throws IOException {
        //1 创建socket对象，指定端口
        DatagramSocket get = new DatagramSocket(10086);
        //2 创建包裹
        DatagramPacket pg = new DatagramPacket(new byte[1024],1024);
        //3,阻塞时方法，调用此方法会造成程序阻塞，一直处于接收状态，直到收到数据
        get.receive(pg);
        //4 解析数据，并输出
        String ip = pg.getAddress().getHostAddress();
        String name = pg.getAddress().getHostName();
        System.out.println(ip+":" + name +  ":"  +new String(pg.getData(),0,pg.getLength()));
        System.out.println("接受数据大小："+pg.getLength()+" | 包裹容量:"+pg.getData().length);
        //5关闭资源
        get.close();
    }
}
