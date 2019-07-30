package org.seefly.thread.lev1;
/**
 * wait：
 *      此方法只能使用在同步代码块中，当调用此方法时当前线程会释放手中的锁然后进入该锁的等待池。
 *      它还有一个重载的方法，若给予指定的时间，当时间结束后还没有被唤醒，那么此线程将会自动进入锁池，去争夺拥有该锁的权利。
 * notify:
 *      唤醒方法属于Object类，当调用此方法的时候，该锁对应的等待池中的随机一个线程会被唤醒进入锁池
 *      在锁池的线程拥有争夺这个锁的权利，当取得该锁后才会由执行的资格。nofity是随机唤醒一个等待线程
 *      具体是哪一个则是由jvm决定的,所以这种唤醒是不公平的，并不是说先等待的就一定被先唤醒。而notifyAll则是唤醒等待池中所有的线程进入锁池。
 *
 * 注意，这两个方法只能使用在synchronized块或方法中。因为在它之外就没有锁这个概念了。而这两个方法正是基于锁而操作的。
 *
 * 效果是input之后执行output，实现了交替工作。而不是由CPU随机分配资源
 *
 * @author 刘建鑫
 * */
public class B3_WaitNotify {
	public static void main(String[] args){
		Res r = new Res();

		new Thread(new Input(r)).start();
		new Thread(new Output(r)).start();
	}

    /**
     * 模拟共享资源
     */
    private static class Res{
        private String name;
        private String sex;
        private boolean flag = false;

        public synchronized void set(String name,String sex){
            if(flag){
                try{this.wait();} catch(Exception ex){};
            }
            this.name = name;
            this.sex = sex;
            this.flag = true;
            this.notify();
        }

        public synchronized void out(){
            if(!flag){
                try{this.wait();} catch(Exception ex){};
            }
            System.out.println(this.name+"******"+this.sex);
            this.flag = false;
            this.notify();
        }
    }

    /**
     * 模拟生产者
     */
    private static class Input implements Runnable{
        private Res r;
        private int i = 0;
        Input(Res r){
            this.r = r;
        }
        @Override
        public void run(){
            while(true){
                if(i == 0){
                    r.set("张三", "女");
                }
                else{
                    r.set("Michael", "men");
                }
                i = (i+1)%2;
            }
        }
    }

    /**
     * 模拟消费者
     */
    private static class Output implements Runnable{
        private Res r;
        Output(Res r){
            this.r = r;
        }
        @Override
        public void run(){
            while(true){
                r.out();
            }
        }
    }

}




