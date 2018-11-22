package org.seefly.juc.future;

/**
 * @author liujianxin
 * @date 2018-11-22 11:41
 */
public class FutureData implements Data{
    protected boolean isReady;
    protected RealData realData = null;

    public synchronized void setRealData(RealData realData){
        if(isReady){
            return;
        }
        this.realData = realData;
        isReady = true;
        notifyAll();
    }

    @Override
    public synchronized String getResult(){
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
