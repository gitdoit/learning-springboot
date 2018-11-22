package org.seefly.juc.future;

/**
 * @author liujianxin
 * @date 2018-11-22 11:40
 */
public class RealData implements Data{
    protected String result;

    public RealData(String para) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 10;i++){
            sb.append(para);
            try{
                Thread.sleep(100);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
        result = sb.toString();
    }

    @Override
    public String getResult() {
        return result;
    }
}
