package org.seefly.net.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * 模拟服务器
 * @author liujianxin
 * @date 2019-01-04 14:08
 */

public class ServerDemo {
    private boolean stop = false;

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket accept = serverSocket.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(accept.getOutputStream()), true);
            String line;
            boolean done = false;
            while (!done && (line = br.readLine()) != null) {
                System.out.println(line);
                if ("bye".equals(line)) {
                    done = true;
                }
                pw.println("bye");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        this.stop = true;
    }
}
