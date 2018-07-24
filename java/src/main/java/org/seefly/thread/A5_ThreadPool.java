package org.seefly.thread;

import java.util.concurrent.*;

/**
 * @author ������
 * javaͨ��Executors�ṩ�����̳߳�
 * newCacheThreadPool����һ���ɻ�����̳߳أ������������ģ�����������������Ҫ�����¿����̡߳������ڴ�����Ҫ����ա�
 * newFixedThreadPool����һ�������̶����̳߳أ����������߳���󲢷������������߳��ڶ����еȴ���
 * newScheduledThreadPool ����һ�������̶����̳߳أ�֧�ֶ�ʱ�Լ����ڴ�������
 * newSingleThreadExecutors��������Ϊ1���̳߳أ�ʹ����Ψһ���̴߳������񣬱�֤��������ָ��˳��ִ������(FIFO,LIFO,���ȼ�)
 * http://cuisuqiang.iteye.com/blog/2019372
 * */
public class A5_ThreadPool {
	public static void main(String[] args) {
		// ѭ�������̣߳���FixedThreadPool��װ���̳߳�����Ϊ3������ͬʱ�������߳����ֻ������������ĵȴ�
        // FixedThreadPool.shutdown();�˷�����ʹ�߳�ֹͣ����������(�����������ᱨ��)�����������ִ����ȴ��ж��е�����
        // shutdownNow��ʹ����ִ�е��̴߳����ж�״̬�����ز�����ȴ��ж��е��߳�
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        // ���������̳߳�
        ExecutorService CachePool = Executors.newCachedThreadPool();
		for(int i = 0; i < 100; i++) {
			fixedThreadPool.execute(() -> {
                System.out.println("�̶��̳߳أ�"+Thread.currentThread().getName()+" is running");
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            });
            CachePool.execute(() -> {
                System.out.println("�����̳߳أ�"+Thread.currentThread().getName()+" is running");
            });
		}
		
	}

}


