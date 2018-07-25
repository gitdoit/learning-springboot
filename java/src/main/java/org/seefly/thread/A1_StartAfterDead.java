package org.seefly.thread;
/**
 * @author 刘建鑫
 *一个线程死亡之后不能再通过star()方法试图使他再次运行。否则出现IllegalThreadStateException
 *一个线程在正常结束后，抛出一个未捕获的异常，调用stop后会死亡。
 *线程在新建态以及死亡态时调用isAlive方法时返回false
 * */
public class A1_StartAfterDead {
	public static void main(String[] args) {
		Dead t = new Dead();
		System.out.println("isAlive after new:"+t.isAlive());
		t.start();
		System.out.println("isAlive after start:"+t.isAlive());
		try {
			t.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
        //此处由于使用了join方法，肯定返回false
		System.out.println("isAlive after join:"+t.isAlive());;
        //这里试图使已经死亡的线程重新启动。报错：IllegalThreadStateException
		t.start();
	}

	private static class Dead extends Thread{
        @Override
        public void run() {
            for(int i = 0;i < 10; i++){
                System.out.println(Thread.currentThread().getName()+":"+i);
            }
        }
    }
}


