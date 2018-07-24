package org.seefly.thread;
/*����չʾ��Thread�޷������ȱ�㡣
 * ʵ��A,C��Thread�����࣬���Ƕ�����Ʊ������ʣ��Ʊ���޷�����
 * ��Runnableʵ��B��Ȼ�����������̣߳���������ȴ����ͬһ��Ʊ��
 * �Ժ�Ҫʹ��Runnable�����߳�
 * 
 * ʵ�ֹ���ķ�����
 * ������ʵ��Runnable�ӿڣ�����ֱ�ӹ���Runnable�ӿ�
 * ��д����run����
 * ͨ��Thread�ཨ���̶߳���
 * ��Runnable�ӿڵĶ�����Ϊʵ�ι���Thread�����
 * ����Thread���start��������������������Runnable�ӿڵ�run����
 * */
public class Runn_share {

	public static void main(String[] args) {
		//Thread������Դ������
		Thr c = new Thr(5);
		Thr a = new Thr(5);
		c.start();
		a.start();
		
		//����Runnable����Ȼ������ʵ�ηŽ�Thread�����С�����ʵ�ֹ���
		Runn b = new Runn(5);
		new Thread(b).start();
		new Thread(b).start();
		
	
		System.out.println(Thread.currentThread().getName());

	}

}



class Thr extends Thread{//�̳�Thread����
	private int count;//Ʊ��
	
	public Thr(){
		count = 0;
	}
	public Thr(int count){
		this.count = count;
	}
	@Override
	public void run(){
		while(count > 0){
			System.out.printf("Thread��ʣ%d��Ʊ\n",count);
			System.out.println(Thread.currentThread().getName());

			count--;
		}
	}
}

class Runn implements Runnable{//�̳�Runnable����
	private int count = 0;
	public Runn(){
		count = 0;
	}
	public Runn(int count){
		this.count = count;
	}
	
	@Override//��д
	public void run(){
		while(count > 0){
			System.out.printf("Runnable��ʣ%d��Ʊ\n",count);
			count--;
		}
	}
}
