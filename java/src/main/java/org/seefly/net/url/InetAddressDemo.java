package org.seefly.net.url;

import java.net.InetAddress;

/**
 * 本类用来演示java是如何封装一个IP地址
 * 可以用InetAddress 中的静态方法将网址封装成一个IP对象
 *
 * PID 电脑中为每一个进程分配的一个ID，它不代表端口号
 * 命令：netstat -nao 查看PID与对应的端口号
 * 0-1024为系统保留，仅供系统进程使用的端口号。
 * 1025-65535为程序使用
 *
 * @author liujianxin
 */
public class InetAddressDemo {
    public static void main(String[] args) throws Exception {
        //根据指定的主机名称，获得地址
        InetAddress ip = InetAddress.getByName("DESKTOP-K7IID8L");
        InetAddress ip2 = InetAddress.getByName("www.baidu.com");
        InetAddress ip3 = InetAddress.getByName("180.97.33.107");
        InetAddress ip4 = InetAddress.getByAddress(new byte[] {117,67,11,17});
        System.out.println(ip2.getHostAddress());
        //判断是否可达
        System.out.println(ip.isReachable(2000));
        //获取实例的IP地址+主机名
        System.out.println(ip.getHostAddress()+"---"+ip.getHostName());
        //对应的全限定域名
        System.out.println(ip.getCanonicalHostName());


    }
}
