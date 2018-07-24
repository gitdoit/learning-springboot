package org.seefly.thread;
/**
 * ������β��ԣ����ܳ���AB��������Ʊ����ͬ�������������������
 * �����������Ԥ�ϣ��������ʺ�С������Ҳ�ᷢ��
 * 
 * �����ԭ��
 * ����������ڲ���ͬһ���̹߳��������ʱ��һ���̶߳Զ������ִֻ����һ���ݡ�
 * ��û��ִ������һ���̲߳������ִ�У����¹������ݴ���
 * �����������run��������a�߳��ڼ���while��ѭ�������ǣ��Ѿ�����жϿ���ִ�С����Ǵ�ʱ����ʱ��Ƭ�����ˣ���ʱ����b�߳����У�����������a�л�δ���ü��ı�
 * b��ʹ��������������ݣ��������˹������ݴ��󡣾��������ݿ��������������
 * 
 * *********************************************************************************************
 * ����취��
 * �Զ��������������ݵ���䣬ֻ����һ���̶߳�ִ���꣬��ִ�й����У������̲߳����Բ���ִ��-----���ݿ⣨Ҫô��ִ�У�Ҫô����ִ�У�
 * synchronized(����)                                  synchronized��ͬ����
 * {
 * 		���������Ҫ���廯�Ĵ��롣��Ҫִ�У���ִ�С�
 * }
 * 
 * Ҳ���Խ��ؼ��֣�synchronized�������η� ���ڷ���ͷ�ϡ�public synchronized void show(){....}
 * 
 * ***************************************************************************************
 * 
 * ���ȷ����Щ������Ҫ�Ž�synchronized�У�
 * 1 ��ȷ��Щ�����Ƕ��߳����д���
 * 2 ��ȷ��������
 * 3 ��ȷ���߳����д�������Щ���ʱ�����������ݵ�
 * */
public class SafeOfThread {
	public static void main(String[] args ){
		Ticket rb = new Ticket(500);
		Thread a = new Thread(rb,"��ƱԱA");
		Thread b = new Thread(rb,"��ƱԱB");
		a.start();
		b.start();
		
	}
}


class Ticket implements Runnable{
	private int tickets;
	Object obj = new Object();
	
	public Ticket(){
		this(0);
	}
	public Ticket(int tickets){
		this.tickets = tickets;
	}
	
	@Override
	public void run(){
		//����ȫ�ķ�ʽ��while�ж���ɺ�ʱ��Ƭ�ù⣬�л������߳�
		/*while(tickets > 0)
		{
			try{Thread.sleep(10);}catch(Exception ex){}
			System.out.println(Thread.currentThread().getName()+":"+this.tickets--);
		}*/
		
		//��ȫ�ķ�ʽ��ͨ����ͬ���������м����ﵽͬ����Ŀ�ġ�
		//Object obj = new Object();
		synchronized(obj){
			while(tickets > 0)
			{
				try{Thread.sleep(10);}catch(Exception ex){}
				System.out.println(Thread.currentThread().getName()+":"+this.tickets--);
			}
		}
	}
}
