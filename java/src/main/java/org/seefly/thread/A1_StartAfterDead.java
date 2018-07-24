package org.seefly.thread;
/**
 * @author ������
 *һ���߳�����֮������ͨ��star()������ͼʹ���ٴ����С��������IllegalThreadStateException
 *һ���߳��������������׳�һ��δ������쳣������stop���������
 *�߳����½�̬�Լ�����̬ʱ����isAlive����ʱ����false
 * */
public class A1_StartAfterDead {
	public static void main(String[] args) {
		Dead t = new Dead();
		System.out.println("isAlive after new:"+t.isAlive());
		t.start();
		System.out.println("isAlive after start:"+t.isAlive());
		try {
			t.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
        //�˴�����ʹ����join�������϶�����false
		System.out.println("isAlive after join:"+t.isAlive());;
        //������ͼʹ�Ѿ��������߳���������������IllegalThreadStateException
		t.start();
	}

	private static class Dead extends Thread{
        @Override
        public void run() {
            for(int i = 0;i < 10; i++){
                System.out.println(Thread.currentThread().getName()+":"+i);
            }
        }
    }
}


