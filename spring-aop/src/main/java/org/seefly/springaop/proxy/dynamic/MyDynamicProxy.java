package org.seefly.springaop.proxy.dynamic;

import org.seefly.springaop.proxy.Call;
import org.seefly.springaop.proxy.CallImpl;
import org.seefly.springaop.proxy.Sms;
import org.seefly.springaop.proxy.SmsImpl;
import org.seefly.springaop.proxy.util.GenerateClassUtil;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理模式
 *
 * @author liujianxin
 * @date 2019/8/29 20:56
 */
public class MyDynamicProxy implements InvocationHandler {
    
    /**
     * 目标对象
     */
    private Object target;
    
    
    /**
     * 通过JDK的动态代理完美的解决了静态代理硬编码违反开闭原则的问题
     */
    public static void main(String[] args) throws IOException {
        MyDynamicProxy proxy = new MyDynamicProxy();
        proxy.setTarget(new SmsImpl());
        Sms instance = (Sms) proxy.getInstance();
        instance.sendSms("110");
        
        System.out.println("--------------------");
        
        Call callImpl = new CallImpl();
        proxy.setTarget(callImpl);
        Call call = (Call) proxy.getInstance();
        call.call("120");
        
        GenerateClassUtil.generateClassFile(call.getClass().getSimpleName(), Call.class);
    }
    
    public Object getInstance() {
        /**
         * ClassLoader 加载代理类的类加载器
         * interfaces 代理的接口
         * InvocationHandler
         */
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }
    
    public void setTarget(Object target) {
        this.target = target;
    }
    
    
    /**
     * InvocationHandler是由代理实例的调用处理程序实现的接口。
     * 每个代理实例都有一个关联的调用处理程序。在代理实例上调用方法时，方法调用将被编码并调度到其调用处理程序的invoke方法。
     *
     * 控制对目标对象的访问.
     * 上面根据接口动态生成的类长这个样子
     * 其中的h 就是创建代理实例的时候传入的当前类
     *
     * public final class $Proxy1 extends Proxy implements Call
     *
     *   //在内存中生成的class会有一个这样的构造函数，并且在Proxy.newProxyInstance(..)可以看到
     *   //我们传进去的InvocationHandle会被当作构造参数来构建这个动态生成的类
     *   public $Proxy1(InvocationHandler var1) throws  {
     *         super(var1);
     *     }
     *
     *   // 然后我们在调用动态生成的类的方法时会执行InvocationHandle.invoke(..)
     *   public final void call(String var1) throws  {
     *           try {
     *               super.h.invoke(this, m3, new Object[]{var1});
     *           } catch (RuntimeException | Error var3) {
     *               throw var3;
     *           } catch (Throwable var4) {
     *               throw new UndeclaredThrowableException(var4);
     *           }
     *       }
     * }
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object invoke = method.invoke(target, args);
        after();
        return invoke;
    }
    
    
    private void before() {
        System.out.println("执行前！");
    }
    
    private void after() {
        System.out.println("执行后！");
    }
}
