package org.seefly.thread;
/**
 * ����������ʾ����ģʽ�µ�����ʽ��ͬ����������ϵ
 * 
 * ������ʽ�Ͷ���ʽ��ʲô��ͬ��
 * 		����ʽ���ص���ʵ������ʱ����
 * ��ô�����᲻����ʲô���⣿
 * 		�У��ڶ��̷߳���ʱ����ְ�ȫ����
 * ��ô�����
 * 		���Լ�ͬ���������������������Щ��Ч�����Լ�˫���ж��������
 * ��ͬ����ʱ��ʹ�õ�������һ����
 * 		�����������ֽ����ļ�����
 * */
public class Synchronized_single {

}

class Single{
	private static Single s = null;
	private Single(){}
	
	public static Single getInstanc(){//����ʽ
		if(s == null)
		{
			synchronized(Single.class)
			{
				if(s == null){
					s = new Single();
				}
			}
		}
		return s;
	}
}

class Single1{//����ʽ
	private static final Single1 s = new Single1();
	private Single1(){};
	public static Single1 getInstance(){
		return s;
	}
}

