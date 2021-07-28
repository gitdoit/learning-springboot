package org.seefly.aop.basic.proxy.dynamic;

import org.junit.Test;
import org.seefly.aop.basic.proxy.interfaces.Call;
import org.seefly.aop.basic.proxy.interfaces.impl.CallImpl;
import org.seefly.aop.basic.util.GenerateClassUtil;

import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * jdk的动态代理模式
 * 创建逻辑:
 *  使用Proxy.newProxyInstance根据提供的接口动态的在内存中创建实现这些接口的动态class
 *  然后通过类加载器加载到内存中.
 *
 * 执行逻辑:
 *   通过动态创建的字节码反编译出来可以看到,构造方法中需要一个InvocationHandler对象
 *   也就是我们传进去的,执行接口的实现方法的时候内部会调用到InvocationHandler.invoke方法,就执行了我们自己的逻辑
 *
 * 注意事项:
 *  不要在InvocationHandler.invoke(proxy,method,args)里面使用proxy调用method
 *  这样会无限循环.
 *
 *
 *
 * @author liujianxin
 * @date 2019/8/29 20:56
 */
public class JdkProxyDemo  {
    
    
    public static void main(String[] arg) {
        CallImpl originalObject = new CallImpl();
        // jkd是根据
        Call proxiedObject =(Call) Proxy.newProxyInstance(
                // 使用的类加载器
                CallImpl.class.getClassLoader(),
                // 目标对象实现的接口
                CallImpl.class.getInterfaces(),
                // 拦截逻辑
                (proxy, method, args) -> {
                    System.out.println("拦截前执行的逻辑...");
                    Object returnValue = method.invoke(originalObject, args);
                    System.out.println("拦截后执行的逻辑");
                    return returnValue;
                });
        proxiedObject.call("110");
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
    @Test
    public void showMeTheClass() throws IOException {
        CallImpl originalObject = new CallImpl();
        // jkd是根据
        Call proxiedObject =(Call) Proxy.newProxyInstance(
                // 使用的类加载器
                CallImpl.class.getClassLoader(),
                // 目标对象实现的接口
                CallImpl.class.getInterfaces(),
                // 拦截逻辑
                (proxy, method, args) -> {
                    System.out.println("拦截前执行的逻辑...");
                    Object returnValue = method.invoke(originalObject, args);
                    System.out.println("拦截后执行的逻辑");
                    return returnValue;
                });
        GenerateClassUtil.generateClassFile("F:\\IOTest\\",proxiedObject.getClass().getSimpleName(), Call.class);
    }
    
    
   
}
