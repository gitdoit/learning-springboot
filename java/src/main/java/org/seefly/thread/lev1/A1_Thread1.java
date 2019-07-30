package org.seefly.thread.lev1;

/**
 * 经过多次测试，可能出现AB会出现最后票数相同，或者卖出负数的情况
 * 这种情况难以预料，发生概率很小，但是也会发生
 * <p>
 * 问题的原因：
 * 当多条语句在操作同一个线程共享的数据时，一个线程对多条语句只执行了一部份。
 * 还没有执行完另一个线程参与进来执行，导致共享数据错误。
 * 就像是下面的run方法，当a线程在计算while的循环条件是，已经完成判断可以执行。但是此时它的时间片用完了，暂时挂起。b线程运行，共享数据在a中还未来得及改变
 * b就使用了这个共享数据，这就造成了共享数据错误。就像是数据库里的两段锁概念
 * <p>
 * *********************************************************************************************
 * 解决办法：
 * 对多条操作共享数据的语句，只能让一个线程都执行完，在执行过程中，其他线程不可以参与执行-----数据库（要么都执行，要么都不执行）
 * synchronized(对象)                                  synchronized：同步的
 * {
 * 这里面放需要整体化的代码。即要执行，都执行。
 * }
 * <p>
 * 也可以将关键字：synchronized当做修饰符 放在方法头上。public synchronized void show(){....}
 * <p>
 * ***************************************************************************************
 * <p>
 * 如何确定哪些代码需要放进synchronized中？
 * 1 明确哪些代码是多线程运行代码
 * 2 明确共享数据
 * 3 明确多线程运行代码中哪些语句时操作共享数据的
 * @author 刘建鑫
 */
public class A1_Thread1 {
    public static void main(String[] args) {
        Ticket rb = new Ticket(500);
        Thread a = new Thread(rb, "售票员A");
        Thread b = new Thread(rb, "售票员A");
        a.start();
        b.start();

    }

    private static class Ticket implements Runnable {
        private int tickets;
        Object obj = new Object();

        public Ticket() {
            this(0);
        }

        public Ticket(int tickets) {
            this.tickets = tickets;
        }

        @Override
        public void run() {
            //不安全的方式，while判断完成后时间片用光，切换其他线程
            /*while(tickets > 0)
            {
                try{Thread.sleep(10);}catch(Exception ex){}
                System.out.println(Thread.currentThread().getName()+":"+this.tickets--);
            }*/

            // 安全的方式，通过对同步代码块进行加锁达到同步的目的。
            synchronized (obj) {
                while (tickets > 0) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                    System.out.println(Thread.currentThread().getName() + ":" + this.tickets--);
                }
            }
        }
    }
}



