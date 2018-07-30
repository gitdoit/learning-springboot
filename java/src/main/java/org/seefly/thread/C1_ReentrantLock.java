package org.seefly.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 刘建鑫
 * 本线程用来演示ReentrantLock的使用方法
 * 注意，上锁解锁一定是成对出现的。而且要要将解锁步骤放进finally块内，保证一定会被执行到。
 * <p>
 * ReentrantLock锁具有可重入性，也就是说一个线程可以对已经被枷锁的ReentrantLock锁再次加锁；
 * 即一段被锁保护的代码，可以调用另一个被相同锁保护的代码
 * Lock lock = new ReentrantLock();
 * lock.lock//加锁
 * try
 * {
 * 需要同步的代码
 * }
 * finally
 * {
 * lock.unlock;//释放锁
 * }
 * *
 */
public class C1_ReentrantLock {

    public static void main(String[] args) {
        BankDemo b = new BankDemo(100, 1000);
        int i;
        for (i = 0; i < 10; i++) {
            TransferRunnable r = new TransferRunnable(b, i, 1000);
            Thread t = new Thread(r);
            t.start();
        }
    }


   private static class BankDemo {
        private int[] accounts;
        private Lock bankLock = new ReentrantLock();

        public BankDemo(int userNum, int initBalance) {
            // 初始化用户数量以及账户余额
            accounts = new int[userNum];
            for (int i = 0; i < accounts.length; i++) {
                accounts[i] = initBalance;
            }
        }

        // 转账业务
        public void transfer(int from, int to, int money) {
            bankLock.lock();
            try {
                if (accounts[from] < money) {
                    return;
                }
                //转出
                accounts[from] -= money;
                System.out.printf("%d块钱从%d转入%d\n", money, from, to);
                //转入
                accounts[to] += money;
                System.out.println("当前银行总存款为：" + getTotalBalance());

            } finally {
                bankLock.unlock();
            }
        }

        public int getTotalBalance() {
            int sum = 0;
            for (int e : accounts) {
                sum += e;
            }
            return sum;
        }
    }


    private static class TransferRunnable implements Runnable {
        private BankDemo bank;
        private int fromAccount;
        private int maxAmount;

        public TransferRunnable(BankDemo b, int from, int max) {
            bank = b;
            //转出的账户
            fromAccount = from;
            //初始存款
            maxAmount = max;
        }

        @Override
        public void run() {

            try {
                int toAcount = (int) (100 * Math.random());
                int amount = (int) (maxAmount * Math.random());
                bank.transfer(fromAccount, toAcount, amount);
                Thread.sleep((int) (10 * Math.random()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



