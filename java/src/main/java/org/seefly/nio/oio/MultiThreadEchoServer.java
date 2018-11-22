package org.seefly.nio.oio;

import org.apache.commons.lang3.time.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 演示传统IO工作方式
 * @author liujianxin
 * @date 2018-11-22 16:53
 */
public class MultiThreadEchoServer {
    private static ExecutorService tp = Executors.newCachedThreadPool();


    public static void main(String[] args) throws IOException {
        ServerSocket echoServer = new ServerSocket(8000);
        Socket clientSocket;
        for (; ; ) {
            clientSocket = echoServer.accept();
            System.out.println(clientSocket.getRemoteSocketAddress() + "已连接！");
            tp.execute(new HandleMsg(clientSocket));
        }
    }


    static class HandleMsg implements Runnable {
        Socket clientSocket;

        public HandleMsg(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter os = new PrintWriter(clientSocket.getOutputStream(), true)) {
                String inputLine;
                StopWatch sw = new StopWatch();
                sw.start();
                while ((inputLine = is.readLine()) != null) {
                    os.println(inputLine);
                }
                sw.stop();
                System.out.println("耗时：" + sw.getTime());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
