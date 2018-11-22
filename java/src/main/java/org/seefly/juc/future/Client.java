package org.seefly.juc.future;

/**
 * 一次请求一次响应C/S，这种情况下在客户端发起请求之后就必须一直等待
 * 服务器响应，在此期间不能做其他事情。
 *
 * 现在使用Future，在发起请求之后客户端可以做其他的事情，然后再后来的某个时间点再去
 * 获取请求结果。就像是在煮汤的同时不用一直等着做好，可以去切菜。然后等个十几分钟再去检查
 * 汤有没有做好。
 * @author liujianxin
 * @date 2018-11-22 13:42
 */
public class Client {
    public Data request(final String queryStr){

        final FutureData futureData = new FutureData();
        // 开启一个新线程，让这个线程等待相应结果
        new Thread(() ->{
            // 模拟发送请求，等待获取响应结果
            RealData realData = new RealData(queryStr);
            // 设置响应结果
            futureData.setRealData(realData);
        }).start();
        return futureData;
    }

    public static void main(String[] args){
        Client client = new Client();
        Data data = client.request("name");
        System.out.println("请求完毕！");
        // 请求完毕之后，去做其他事情
        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        // 做完了 检查响应
        System.out.println("数据："+data.getResult());
    }
}
