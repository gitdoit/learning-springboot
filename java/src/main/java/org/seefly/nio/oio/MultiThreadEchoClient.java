package org.seefly.nio.oio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author liujianxin
 * @date 2018-11-22 17:07
 */
public class MultiThreadEchoClient {

    public static void main(String[] args) throws IOException {
        Socket client = new Socket();
        client.connect(new InetSocketAddress("localhost", 8000));
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter writer = new PrintWriter(client.getOutputStream(), true)) {

            writer.println("hello!");
            writer.flush();
            System.out.println("from server:" + reader.readLine());

        }finally {
            client.close();
        }


    }
}
