package org.seefly.thread;


import lombok.Data;

/**
 * @author ������
 * �����Ŀ������ʾ����̲߳���ִ��ʱ�����⡣
 * �������������Լ����������ߣ����ǵ�Ŀ��������һ������һ����
 * ����ͨ��flag��־λȷ�����������ѽ���ʵ�֡�
 * 
 * ��������߻��������е�while �� notifyAll ���� if �� notify �������������
 * �����if�ж�flag��־λ����ô��ͬ�������ڣ�t1��ʼִ�л����Դ�ж�flagΪ�٣�����wait��ִ�к�����䡣����ʱ��Ƭ�ù�֮ǰ��ִ�е���if����ʱflagΪ��
 * t1�ȴ����ͷ�����t2���ִ����Դ���ж�flagҲΪ�棬Ҳ�ȴ����ͷ�����t3�����Դ�ж� ��flag Ϊ�٣�����wait��
 * ִ�����ѡ��������ʼ�����̳߳ص�t1����������t1��ִ����Դ��Ҫ������Ȼ��t3��ִ����һ��
 * ��if��ʱ���ж�flag������ȴ����ͷ��ʸ����ñ�t4������t4һ�ж�Ҳ����ȵ������ʸ񡣴�ʱֻ��t1���ţ�������֮��������һ����Ʒ��
 * ������t2��֮������һ���жϲ������˵ȴ���ֻ��t2���ţ��õ�����Դ��
 * ��Ϊ�ȴ�֮ǰ�Ѿ��жϹ�flag������ֱ������ִ�У�Ҳ������һ����Ʒ��������ͳ�������������������Ʒ������
 * 
 * */
public class B5_NotifyAll {

	public static void main(String[] args){
	    final Resource res = new Resource("��Ʒ");

		new Thread(()->{while (true){res.production();} },"������1��").start();
		new Thread(()->{while (true){res.production();}},"������2��").start();

		new Thread(()->{while (true){res.consume();}},"������1��").start();
		new Thread(()->{while (true){res.consume();}},"������2��").start();

	}


    /**
     * ������Դ
     */
    @Data
	private static class Resource{
        private String name;
        private int count = 0;
        private boolean flag = false;

        public Resource(String name){
            this.name = name;
        }

        /**
         * �������������ݱ�־λ�ж���Ʒ�Ƿ����ѵ��ˡ�
         * ��û�б�������ȴ�
         */
        public synchronized void production(){
            while(flag){
                try{this.wait();} catch(Exception ex){}
            }
            System.out.println(Thread.currentThread().getName()+" ������һ��" + name + "�����Ϊ��" + count++);
            this.flag = true;
            this.notifyAll();
        }

        /**
         * �����߷��������ݱ�־λ�ж��Ƿ�����Ʒ���Ա�����
         * ��û�п����ѵ���Ʒ��ȴ�
         */
        public synchronized void consume(){
            while(!flag){
                try{this.wait();} catch(Exception ex){}
            }
            System.out.println(Thread.currentThread().getName()+" ������һ��" + name + "�����Ϊ��" + count);
            this.flag = false;
            this.notifyAll();

        }

    }
}




