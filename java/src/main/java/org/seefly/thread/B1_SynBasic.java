package org.seefly.thread;
/**
 * ����������ʾsynchronized����������
 * 
 * Ticket1��̳�Runnable�ӿڣ����һ�����Դ���Ʊ������дrun������
 * ��������һ��ͬ��������ͬ��������ͨ������flagֵʹ�߳�ִ�в�ͬ��ͬ������
 * 
 * ֵ��ע�����synchronized���Ĳ����Ǵ��룬֮ǰһֱ��Ϊsynchronized�ǽ����ڴ������ϳ�һ�����ִ�����ﵽͬ����Ŀ�ģ��������ݿ��������ԭ���Ը��
 * ������ʵ���������������ӣ���ʵ�����cup���������߳�������Դ������߳������һ��ʱ��Ƭ��û��ִ��
 * ��ͬ��������ڵ����ݣ�ʱ��Ƭ����֮�������ǻᱻ��������ô��Ȼ�����������ôʵ��ͬ�����أ�
 * ��ʵÿһ���߳���ִ��ͬ��������ͬ��������ʱ�򶼻�ͨ��synchronized�Ĳ�����һ���������ж��Լ��Ƿ����ʸ�ִ�����д��롣�����������൱������
 * ��ÿ������ֻ����һ�����������������߳�ִ��ͬ
 * һ��Runnableʵ���е�run��������run��������һ��synchronizedͬ���飬�����Ϊthis��Runnableʵ������
 * �߳�A���ȵõ�������Դ��ִ��run�������������е�ͬ������飬���ȼ��synchronized�Ĳ�����this�����ڸ�ͬ�����������Ƿ�ȡ�ߡ����û��ȡ����A���this���ڸ�
 * ͬ������������Ȼ��ִ���������ݣ�һ��ʱ���ʱ��Ƭ���꣬����ȴû��ִ����ͬ�����е����ݡ�
 * ��ʱ���߳�A��������this���ڸ�ͬ����������������ͷš����߳�B���������Դ֮��Ҳ���߳�Aһ������Ƿ���ִ���ʸ񣬷���û����ȴ���ֱ��ʱ��Ƭ����Ҳ����ִ�С�
 * 
 * ���Խ�synchronized�Ĳ������Ϊһ�����壬�����������������ʡ���ͬһ��ͬ�������ȴ�в�ͬ��������ôҲ����ֶ��̲߳���ִ�иô����������ֻ�е������߳�
 * ʹ�ö������ͬ��������ͬһ������ʱ��Ż�ﵽͬ����Ŀ�ġ�
 * 
 * 
 * ��ͬ����������̬֮����ô�������Ͳ�����this����Ϊ�ھ�̬���������ڴ�ʱ��û�����ɶ��󣬸�û�������������������ʹ�õ��� ����.class������Ϊ��������
 * 
 * */
public class B1_SynBasic {
	public static void main(String[] args) {
		Ticket1 p = new Ticket1();
		//Ticket1 p1 = new Ticket1();
		Thread t1 = new Thread(p,"t1");
		Thread t2 = new Thread(p,"t2");
		
		t1.start();
		try{
		    Thread.sleep(100);
		}catch(Exception ex){}
		p.flag = false;
		t2.start();

		try{
		    Thread.sleep(1300);
		}catch(Exception ex){}
		t1.stop();
		t2.stop();
	}

	private static class Ticket1 implements Runnable{
        private int tick = 100;
        boolean flag = true;
        @Override
        public void run(){
            if(flag){
                while(true){
                    synchronized(this){
                        if(tick > 0){
                            try{Thread.sleep(10);}catch(Exception ex){}
                            System.out.println(Thread.currentThread().getName()+"....code:"+tick--);
                        }
                    }
                }
            }
            else{
                while(true){
                    show();
                }
            }
        }
        public synchronized void show(){
            {
                if(tick > 0){
                    try{Thread.sleep(10);}catch(Exception ex){}
                    System.out.println(Thread.currentThread().getName()+"******shou:"+tick--);
                }
            }
        }
    }

}

