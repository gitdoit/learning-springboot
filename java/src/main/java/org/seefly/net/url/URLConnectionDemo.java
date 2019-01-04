package org.seefly.net.url;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * URL:统一资源定位器
 * 组成：协议名-主机-端口-资源
 * 	   protocol://host:port/resourceName
 * 	   http://www.crazyit.org/index.hph
 * 注：URI不能定位任何资源，他唯一的作用就是解析
 *
 *多线程下载：
 *  指定网络中的源文件，建立链接。测试链接。
 *  得到源文件大小，n个线程则将文件分成n份，每份大小相同。
 *  为每个线程分别指定一个随机读写输入流，随机读写输出流。并分别设置输入流与输出流的其实位置。
 * @author liujianxin
 */
public class URLConnectionDemo {



    public static void main(String[] args) throws Exception {
        long star = System.currentTimeMillis();
        final DownUtil downUtil = new DownUtil("http://issuecdn.baidupcs.com/issue/netdisk/yunguanjia/BaiduNetdisk_6.7.0.8.exe",
                "E:\\test\\百度云.exe",4);
        downUtil.download();

        // 下载进度条
        new Thread(() -> {
            while(downUtil.getCompleteRate() < 1) {
                System.out.println("进度："+downUtil.getCompleteRate() * 100 +"%");
                try {
                    Thread.sleep(1000);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

class DownUtil{
    //源文件地址
    private String path;
    //存储位置
    private String targetFile;
    //线程数
    private int threadNum;
    //线程数组
    private DownThread[] threads;
    //原文件大小
    private int fileSize;
    //构造方法
    public DownUtil(String path,String targetFile,int threadNum) {
        this.path = path;
        this.targetFile = targetFile;
        this.threadNum = threadNum;
        threads = new DownThread[threadNum];
    }
    //执行下载方法体
    public void download() throws Exception{
        //指定文件地址
        URL url = new URL(path);
        //打开输入流
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置链接超时
        conn.setConnectTimeout(5000);
        //得到源文件大小
        fileSize = conn.getContentLength();
        //关闭链接
        conn.disconnect();
        //将源文件分成与线程数相等的块数，每块长度相等。currentParSiz为每块大小
        int eachPartSize = fileSize / threadNum +1;
        //新建本地文件
        RandomAccessFile file = new RandomAccessFile(targetFile,"rw");
        //设置本地文件大小，要与源文件大小一致
        file.setLength(fileSize);
        file.close();
        for(int i = 0; i < threadNum; i++) {
            //计算每个线程下载的开始位置
            int startPos = i * eachPartSize;
            //为每个线程创建 一个输出流
            RandomAccessFile outputRan = new RandomAccessFile(targetFile,"rw");
            //为每个线程输出流指定开始写入位置
            outputRan.seek(startPos);
            //创建下载线程，传入起始位置，下载的长度，指定输出流
            threads[i] = new DownThread(startPos,eachPartSize,outputRan);
            threads[i].start();
        }


    }
    //获取下载进度
    public double getCompleteRate() {
        int sumSize = 0;
        for(int i = 0; i < threadNum; i ++) {
            sumSize +=threads[i].length;
        }
        return sumSize * 1.0 / fileSize;
    }

    private class DownThread extends Thread{
        private int startPos;
        private int currentPartSize;
        private RandomAccessFile currentPart;
        private int length;
        private DownThread(int startPos,int currentPartSize,RandomAccessFile currentPart){
            this.startPos = startPos;
            this.currentPartSize = currentPartSize;
            this.currentPart = currentPart;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //设置链接超时
                conn.setConnectTimeout(5000);
                //输入流为源文件
                InputStream inStream = conn.getInputStream();
                //设置每个线程开始读入的位置
                inStream.skip(this.startPos);
                byte[] buffer = new byte[1024];
                int hasRead = 0;
                //若以下载长度超过每个线程指定下载长度，或读取完毕。则退出循环
                while(length < currentPartSize && (hasRead = inStream.read(buffer)) != -1) {
                    currentPart.write(buffer,0,hasRead);
                    length += hasRead;
                }
                currentPart.close();
                inStream.close();

            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
