package org.seefly.springredis.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据redis的通讯协议，自己实现一个客户端demo，用于
 * 发送一个简单的请求和解析其响应
 *
 * 以此来了解redis的通讯协议原理
 * @author liujianxin
 * @date 2022/11/01 11:59
 */
public class MyRedisClient {


    /**
     * 协议规范
     *   set name seefly
     * 按照协议会转换成：
     *   *3\r\n
     *   $3\r\nset\r\n
     *   $4\r\nname\r\n
     *   $6\r\nseefly\r\n
     *
     *   +ok\r\n
     *   -Error msg\r\n
     *   :10\r\n
     *   $5\r\nhello\r\n
     *
     * 其中每行的开头代表操作符
     * *表示后面的数据是一个数组，数组长度为3
     * $表示后面是一个多行字符串
     * :表示后面是一个整形数字
     * -表示后面是一行错误消息
     * +表示后面是一行消息
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = null;
        PrintWriter pr= null;
        Socket socket = null;
        // 创建链接
        try {
             socket = new Socket("localhost", 49153);
            // 获取服务端响应
             br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 输出流
             pr = new PrintWriter(socket.getOutputStream());
            // 发出请求
            sendReq(pr,"auth","redispw");
            System.out.println(readResponse(br));

            sendReq(pr,"set","name","seefly");
            System.out.println(readResponse(br));

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(pr != null) {
                pr.close();
            }
            if(br != null) {
                br.close();
            }
            if(socket != null) {
                socket.close();
            }
        }


    }

    public static Object readResponse(BufferedReader br) throws IOException {

        int opt = br.read();
        switch (opt) {
            // 直接读一行
            case '+':
                return br.readLine();
            // 服务端出错，直接读取一行获取报错信息
            case '-':
                throw new RuntimeException(br.readLine());
            // 读取一个整形数字
            case ':':
                return Long.parseLong(br.readLine());
            // 读取多行字符串
            case '$':
                int len = Integer.parseInt(br.readLine());
                if(len == -1) {return null;}
                if(len == 0) {return "";}
                return br.readLine();
            // 读取数组
            case '*':
                return handleBulk(br);
            default:
                throw new RuntimeException("Bad data recv");
        }
    }

    private static Object handleBulk(BufferedReader br) throws IOException {
        int len = Integer.parseInt(br.readLine());
        if(len <= 0) {
            return null;
        }
        List<Object> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            list.add(readResponse(br));
        }
        return list;
    }


    public  static void sendReq(PrintWriter pr,String ...args){
        if(args != null && args.length>0) {
            int size  = args.length;
            pr.println("*"+size);
            for (String arg : args) {
                pr.println("$"+arg.getBytes(StandardCharsets.UTF_8).length);
                pr.println(arg);
            }
        }
        pr.flush();





    }
}
