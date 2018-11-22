package org.seefly.juc.future;

/**
 * @author liujianxin
 * @date 2018-11-22 11:41
 */
public class FutureData implements Data{
    protected boolean isReady;
    protected RealData realData = null;

    public synchronized void setRealData(RealData realData){
        // 防止重复设置
        if(isReady){
            return;
        }
        this.realData = realData;
        isReady = true;
        // 响应完成，唤醒等待线程
        notifyAll();
    }

    @Override
    public synchronized String getResult(){
        // 服务器还没有响应的时候，进入锁池等待
        while (!isReady){
            try {
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return realData.result;
    }

}
