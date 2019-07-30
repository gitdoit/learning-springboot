package org.seefly.thread.lev1;


/**
 * 两个线程并发的对count进行--操作并打印。如果线程A在执行到打印语句的时候时间片用完，它会保存现场信息
 * 如果此时count=5，由于还没有执行打印语句，所以没有执行--操作。所以共享信息count没有发生改变
 * 线程B获得执行资源，此时线程B中的count还是为5.打印“5”并使count=4；休眠，线程A获得执行资源
 * 执行打印语句打印“5”，并使count=count - 1；即count = 4；则会出现连续打印两个相同数的情况。
 * 即两个线程没有同步，如果在判断while循环时count = 0，可以执行。但是判断完之后时间片用完
 * 线程B进来，打印并修改了count值，休眠。线程A执行，在打印语句中重新读取count值此时count被B修改为-1，则会打印-1
 *
 * system.out.println(i--);此语句执行顺序是，先将i复制进输出缓冲区，再将i进行减一。然后再打印。
 * @author 刘建鑫
 */
public class B6_UnSys {

	public static void main(String[] args) {
		Bank a = new Bank();
		Thread t1 = new Thread(a);
		Thread t2 = new Thread(a);
		t1.start();
		t2.start();
		//由于计算机cpu有多个核心，所以这两个线程是真正意义上的并发，而不是利用时间片轮转实现的伪并发

	}
    /**
     * 步骤，判断循环条件，执行打印语句，更改count值，休眠。
     */
    private static class Bank implements Runnable{
        private int count = 100;

        @Override
        public void run() {
            //无同步，a,b可同时进入并对共享数据进行操作，开始while判断，条件为真。
            while(count >=  0) {
                //a,b执行打印语句，100，100
                System.out.println(Thread.currentThread().getName()+":"+"count="+count);
                //先取count放入方法区进行修改，再放回(这个操作在汇编中只有三步，所以时间片在此用完的几率很小，但也有可能)，b再取修改之后的值，再对之修改
                //所以此时count被连续减了两次。所以打印的绝大部分情况都是10，10，8，8...这种情况
                //但是有时候也会出现一片的-1递减，可能是两个线程在不同cpu内核中内获取时间片的的时间恰好错开了，出现了交替打印修改的情况
                count--;
                try {Thread.sleep(10);} catch (Exception e) {}
            }
        }
    }

}

