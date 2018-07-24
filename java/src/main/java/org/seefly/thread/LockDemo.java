package org.seefly.thread;
/*
 * 本线程用来演示ReentrantLock的使用方法
 * 注意，上锁解锁一定是成对出现的。而且要要将解锁步骤放进finally块内，保证一定会被执行到。
 * 
 * ReentrantLock锁具有可重入性，也就是说一个线程可以对已经被枷锁的ReentrantLock锁再次加锁；
 * 即一段被锁保护的代码，可以调用另一个被相同锁保护的代码 
 * Lock lock = new ReentrantLock();
 * lock.lock//加锁
 * try
 * {
 * 		需要同步的代码
 * }
 * finally
 * {
 * 		lock.unlock;//释放锁
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
		fromAccount = from;//转出的账户
		maxAmount = max;//初始存款
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
		accounts = new int[userNum];//初始化用户数量
		for(int i = 0;i < 100;i++)
			accounts[i] = initBalance;
	}
	
	public void transfer(int from,int to,int money) {
		bankLock.lock();
		try {
			if(accounts[from] < money)return;
			accounts[from] -=money;//转出
			System.out.printf("%d块钱从%d转入%d\n",money,from,to);
			accounts[to] +=money;//转入
			System.out.println("当前银行总存款为："+getTotalBalance());
			
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