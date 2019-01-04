package org.seefly.springnetwork.websocket;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @author liujianxin
 * @date 2019-01-04 11:37
 */
public class WebSocketTest {
    // appid
    private   String APPID;
    // appid对应的secret_key
    private   String SECRET_KEY ;
    // 请求地址
    private static final String HOST = "rtasr.xfyun.cn/v1/ws";
    private static final String BASE_URL = "ws://" + HOST;
    private static final String ORIGIN = "http://" + HOST;
    // 音频文件路径
    private static final String AUDIO_PATH = "D:\\16k.pcm";
    // 每次发送的数据大小 1280 字节
    private static final int CHUNCKED_SIZE = 1280;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");


    @Test
    public void before() throws IOException {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        Resource resource = defaultResourceLoader.getResource("classpath:privateConfig.properties");
        Properties p = new Properties();
        p.load(resource.getInputStream());
        this.APPID = p.getProperty("appid");
        this.SECRET_KEY = p.getProperty("appkey");
    }



    @Test
    public void test1() throws Exception {
        String header = "GET /v1/ws?appid=5c2c253e&ts=1546417118&signa=Y06HZtUA3OS0tVBqapTU9iwDPBg%3D HTTP/1.1\r\n" +
                "Connection: Upgrade\r\n" +
                "Host: rtasr.xfyun.cn\r\n" +
                "Origin: http://rtasr.xfyun.cn/v1/ws\r\n" +
                "Sec-WebSocket-Key: wJ/tTlGy7tbZHqoZ44FkSQ==\r\n" +
                "Sec-WebSocket-Version: 13\r\n" +
                "Upgrade: websocket\r\n" +
                "\r\n";
        byte[] httpheader = header.getBytes(StandardCharsets.US_ASCII);
        ByteBuffer bytebuffer = ByteBuffer.allocate(httpheader.length);
        URI uri = new URI(BASE_URL + getHandShakeParams(APPID, SECRET_KEY));

        Socket socket = new Socket();
        socket.setTcpNoDelay( false );
        socket.setReuseAddress( false );
        socket.connect( new InetSocketAddress( uri.getHost(), 80 ), 300 );
        InputStream istream = socket.getInputStream();
        OutputStream ostream = socket.getOutputStream();
        ostream.write(httpheader);
        BufferedReader br = new BufferedReader(new InputStreamReader(istream));

        String s = br.readLine();
        System.out.println(s);
    }


    public static String getHandShakeParams(String appId, String secretKey) {
        String ts = System.currentTimeMillis()/1000 + "";
        String signa = "";
        try {
            signa = HmacSHA1Encrypt(MD5(appId + ts), secretKey);
            return "?appid=" + appId + "&ts=" + ts + "&signa=" + URLEncoder.encode(signa, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws SignatureException {
        byte[] rawHmac = null;
        try {
            byte[] data = encryptKey.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(data, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            byte[] text = encryptText.getBytes("UTF-8");
            rawHmac = mac.doFinal(text);
        } catch (InvalidKeyException e) {
            throw new SignatureException("InvalidKeyException:" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureException("NoSuchAlgorithmException:" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("UnsupportedEncodingException:" + e.getMessage());
        }
        String oauth = new String(Base64.encodeBase64(rawHmac));

        return oauth;
    }

    public final static String MD5(String pstr) {
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = pstr.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) { // i = 0
                byte byte0 = md[i]; // 95
                str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
                str[k++] = md5String[byte0 & 0xf]; // F
            }

            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
