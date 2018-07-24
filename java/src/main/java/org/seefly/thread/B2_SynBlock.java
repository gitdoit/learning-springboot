package org.seefly.thread;

import org.junit.Test;

/**
 * @author ������
 * ������ʾ�˶���ͬһ��ͬ�������,ʹ�ò�ͬ�����������ͬ��Ч����
 * */
public class B2_SynBlock {

    /**
     * �������߳�ʹ�ò�ͬ����ִ����ͬ��ͬ������飬��ô����̻߳Ტ��ִ�����ͬ�������
     */
	@Test
	public void testDiffrentBlock(){
        Runnable1 p = new Runnable1();
        Thread t1 = new Thread(p,"t1");
        Thread t2 = new Thread(p,"t2");
        t1.start();
        t2.start();
    }

	private static class Runnable1 implements Runnable{
        private int count = 100;
        @Override
        public void run(){
            //ÿһ���߳̽��������½�һ������ÿ���̵߳�������һ��
            Object a = new Object();
            synchronized(a){
                while(count > 0){
                    try{Thread.sleep(10);}catch(Exception ex){}
                    System.out.println(Thread.currentThread().getName()+ "   ******   "+count--);
                }
            }
        }
    }







    /**
     * ���ڲ�ͬ��ͬ������飬���ʹ����ͬ��������ô��ʹ�ж���߳�����Ҳ���Ტ��ִ�С�
     */
    @Test
    public void testSameBlock(){
        Runnable2 p = new Runnable2();
        Thread t1 = new Thread(p,"t1");
        Thread t2 = new Thread(p,"t2");
        t1.start();
        t2.start();
    }

    private static class Runnable2 implements Runnable{
        private int a = 100;
        private int b = 100;
        Object c = new Object();
        @Override
        public void run() {
            if (Thread.currentThread().getName().compareTo("t1") == 0) {
                //ʹ��this�ؼ��ִ�������߱�������ÿ���߳�ʹ�õĶ���ͬһ����
                synchronized (this) {
                    while (a > 0) {
                        try {
                            Thread.sleep(10);
                        } catch (Exception ex) {
                        }
                        System.out.println(Thread.currentThread().getName() + "   ******   " + a--);
                    }
                }
            } else {
                synchronized (this) {
                    while (b > 0) {
                        try {
                            Thread.sleep(10);
                        } catch (Exception ex) {
                        }
                        System.out.println(Thread.currentThread().getName() + "   ******   " + b--);
                    }
                }
            }
        }
	}


}


