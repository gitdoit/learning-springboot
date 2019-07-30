package org.seefly.thread.lev1;

/**
 * Thread的定义：public class Thread extends Object implements Runnable
 * Thread构造方法:public Thread(Runnable target,String name)
 * 				public Thread(Runnable target)
 * Thread中有两个重要方法，一个是自身的start,可以通过该函数启动线程，但实际上启动的是run函数，以运行run内自定义的函数体
 * 另一个是run，可以看出是实现了Runnable接口。
 * 有两种启动线程的方式，以下代码可以体现
 *
 * interface Runnable
 * 接口Runnable中只有一个抽象方法，public abstract run();
 * 实现该抽象方法的类实现run，其中放线程需要跑的东西
 *
 * 注意，若定义好一个线程对象之后，如果直接调用run方法，那么他就相当于没有开启线程，还是在主线程内执行，这就和普通的调用没有区别了
 * 只有调用start方法才能开启一个新线程
 * @author 刘建鑫
 */
public class A1_ThreadClass {
	public static void main(String[] args){
		//实例化Thread的一个子类实体
		ThreadDemo A = new ThreadDemo("A");
        //通过A内的方法start直接启动线程
		A.start();
		//实例化接口Runnable
		RunnableDemo B = new RunnableDemo("B");
        //由于实例B为Runnable接口实例，自身无法启动。可通过实例化Thread对象，将Runnable参数传入启动
		new Thread(B).start();

        //有参数构造。实例化匿名线程，在参数里直接实例化实参
		new Thread(new Runnable(){
		    @Override
			public void run(){
				System.out.print("匿名线程，匿名参数");
			}
		}).start();


        //无参构造，实例化的同时覆写其中run方法
		new Thread(){
		    @Override
			public void run(){
				System.out.println("匿名线程，复写run");
			}
		}.start();
	}



    /**
     * 子类直接继承Thread方法，
     */
   private static class ThreadDemo extends Thread{
        private String name;
        public ThreadDemo(String name){
            this.name = name;
        }
        @Override
        public void run(){
            for(int i = 0 ; i < 5 ; i++){
                System.out.println(name + "run" + i);
            }
        }
    }


    /**
     * 实现Runnable接口的类
     */
    private static class RunnableDemo implements Runnable{
        private String name;
        public RunnableDemo(String name){
            this.name = name;
        }
        @Override
        public void run(){
            for(int i = 0; i < 5; i++){
                System.out.println(name + "Runnable" + i);
            }
        }
    }
}


