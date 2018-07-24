package org.seefly.thread;
/*
 * ���߳�������ʾReentrantLock��ʹ�÷���
 * ע�⣬��������һ���ǳɶԳ��ֵġ�����ҪҪ����������Ž�finally���ڣ���֤һ���ᱻִ�е���
 * 
 * ReentrantLock�����п������ԣ�Ҳ����˵һ���߳̿��Զ��Ѿ���������ReentrantLock���ٴμ�����
 * ��һ�α��������Ĵ��룬���Ե�����һ������ͬ�������Ĵ��� 
 * Lock lock = new ReentrantLock();
 * lock.lock//����
 * try
 * {
 * 		��Ҫͬ���Ĵ���
 * }
 * finally
 * {
 * 		lock.unlock;//�ͷ���
 * }
 * 
 *  * */
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public	class LockDemo {
	public static void main(String[] args) {
		BankDemo b = new BankDemo(100,1000);
		int i;
		for(i = 0;i < 10;i++) {
			TransferRunnable r = new TransferRunnable(b,i,1000);
			Thread t = new Thread(r);
			t.start();
		}
	}
}

class TransferRunnable implements Runnable{
	private BankDemo bank;
	private int fromAccount;
	private int maxAmount;
	
	public TransferRunnable(BankDemo b,int from,int max) {
		bank = b;
		fromAccount = from;//ת�����˻�
		maxAmount = max;//��ʼ���
	}
	
	@Override
	public void run() {

		try {
			int  toAcount = (int)(100 * Math.random());
			int amount = (int)(maxAmount * Math.random());
			bank.transfer(fromAccount, toAcount, amount);
			Thread.sleep((int)(10 * Math.random()));
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class BankDemo{
	private int[] accounts;
	private Lock bankLock = new ReentrantLock();
	public BankDemo(int userNum,int initBalance) {
		accounts = new int[userNum];//��ʼ���û�����
		for(int i = 0;i < 100;i++)
			accounts[i] = initBalance;
	}
	
	public void transfer(int from,int to,int money) {
		bankLock.lock();
		try {
			if(accounts[from] < money)return;
			accounts[from] -=money;//ת��
			System.out.printf("%d��Ǯ��%dת��%d\n",money,from,to);
			accounts[to] +=money;//ת��
			System.out.println("��ǰ�����ܴ��Ϊ��"+getTotalBalance());
			
		} finally {
			bankLock.unlock();
		}
	}
	
	public int getTotalBalance() {
		int sum = 0;
		for(int e:accounts)
			sum += e;
		return sum;
	}
}