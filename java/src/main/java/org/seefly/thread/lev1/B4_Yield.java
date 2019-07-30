package org.seefly.thread.lev1;
/**
 * yield()方法是交出自己的执行权限，使自己从执行态强制转换为就绪态，即重新抢夺资源。
 * 好不容易拿到手的资源被yield给夺走了，且让出的执行权只能被同等或高等优先级的线程抢夺。(但当此时没有满足此条件的线程时，则低优先级的线程也会得到执行权)
 * 这更想是使被调用线程暂停一下
 * 注意，它与sleep方法不同，sleep方法是使该线程强制进入阻塞态，经过指定时间后才转换为就绪态。
 * 而yield方法，使该线程从执行态转为就绪，而不阻塞。
 * 很显然，若在同步代码块内调用yield那么该线程不会释放锁
 *
 * @author 刘建鑫
 * */
public class B4_Yield {
	public static void main(String[] args) {
		YieldRun r = new YieldRun();
		Thread t = new Thread(r,"MAX_PRIORITY");
		Thread t2 = new Thread(r,"MIN_PRIORITY");
        //设置线程优先级，JAVA中将之分为10级，且提供了三个优先级常量。但在不同的OS中都不尽相同，或者没有优先级。
        //像是微软的OS，将优先级分为7级。更高的优先级代表着更多的执行机会
		t.setPriority(Thread.MAX_PRIORITY);
		t2.setPriority(Thread.MIN_PRIORITY);
		t.start();
		t2.start();
	}

	private static class YieldRun implements Runnable{
        @Override
        public void run() {
            int i = 0;
            while(i < 50) {
                //当 i == 20时，都会暂停一下
                if(i == 20){
                    Thread.yield();
                }
                System.out.println(Thread.currentThread().getName()+"  :"+i++);
            }

        }
    }
}


