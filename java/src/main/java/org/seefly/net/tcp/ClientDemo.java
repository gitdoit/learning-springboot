package org.seefly.net.tcp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * 模拟客户端
 * @author liujianxin
 * @date 2019-01-04 13:34
 */
public class ClientDemo {

    private Socket socket;

    public ClientDemo(String host, Integer port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMes(String mes) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FilterOutputStream(outputStream)),true);
            Scanner scanner = new Scanner(socket.getInputStream(),"UTF-8");
            String nextLine;
            do {
                bw.println(mes);
                nextLine = scanner.nextLine();
            } while (!"bye".equals(nextLine));
            socket.shutdownOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
