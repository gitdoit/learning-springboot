package org.seefly.thread;

/**
 * @author ������
 */
public class A4_ThreadGroup {

	public static void main(String[] args) {
		// �½��߳��飬ָ���丸�߳���Ϊ���߳��顣
		// ��ʹ�������߳�δ�����쳣���߳����uncaughtException�����ˣ���ô�����쳣���߳�Ҳ��ֹͣ�����쳣�����ϼ�
        // ����������߳�����쳣����������ôȫ�ֵ��쳣���񽫲���Դ��߳���������
		ThreadGroup g = new ThreadGroup(Thread.currentThread().getThreadGroup(),"MyThreadGroup") {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("�߳���g��δ�����쳣������,�쳣�߳�Ϊ"+t.getName()+"  "+e);

				//�������׳����쳣�������κγ��򲶻�
				//throw new RuntimeException("�߳����Զ����쳣�����׳������쳣��");
                //int i = 2 / 0;
			}
		};

		// ���߳���û�ж����Լ����쳣�����������߳�����쳣��������ȫ�ֵ��쳣�����������Ჶ��Ӹ��߳������׳����쳣
        ThreadGroup g2 = new ThreadGroup(Thread.currentThread().getThreadGroup(),"MyThreadGroup2");
        new Thread(g2,() ->{
            int i = 1 / 0;
        },"t2").start();

        //�����Ƿ�Ϊ��̨�߳��飬���������߳̽��ܸı�
		g.setDaemon(true);
		
		// �����̣߳�ָ���������߳����Լ�����
		Thread t1 = new Thread(g,() ->{
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                throw  new RuntimeException("sleep��ʱ���ж��ˣ�����");
            }
        } ,"t1");
		
		Thread t2 = new Thread(g,() ->{
            while(true){
            }
        },"t2");


		//���Ჶ�����еĴ��߳����׳����쳣
		Thread.setDefaultUncaughtExceptionHandler((thread,throwable) ->
                System.out.println("ȫ���߳��쳣����"+thread+"====="+throwable)
        );

		t1.start();
		t2.start();
		ThreadGroup g1 = Thread.currentThread().getThreadGroup();
		System.out.println("���߳�������:"+g1.getName()+"�Ƿ�Ϊ��̨�߳�"+g1.isDaemon());
		System.out.println("�Զ����߳�������:"+g.getName()+"�Ƿ�Ϊ��̨�߳�"+g.isDaemon());
		System.out.println("�߳��飺"+g.getName()+"����߳���Ϊ"+g.activeCount());
        //����߳����б��������̣߳���ô���ø÷��������ñ��������߳��׳��쳣���¸��߳̽�����
        //����߳����������̶߳�û�б���������ô���ô˷����������κ�����
		g.interrupt();
		try {
			Thread.sleep(30);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("����interrupt֮�����߳���Ϊ:"+g.activeCount());
	}
}

