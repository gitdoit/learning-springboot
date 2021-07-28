package org.seefly.aop.basic.proxy.dynamic;

import org.junit.Test;
import org.seefly.aop.basic.proxy.interfaces.NoInterfaceObj;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.io.IOException;
import java.util.Arrays;

/**
 * Jdk动态代理和CGlib动态代理不一样的地方是
 *  1、CGLib动态代理使用的是动态在内存中产生一个继承源Class的类,并创建一个实例
 *  2、CGLib对目标方法的调用不是通过反射，而是通过直接引用的方法调用
 *
 * 所以对于被final修饰的类，是不能用CGLIB的
 * 更多操作：https://www.runoob.com/w3cnote/cglibcode-generation-library-intro.html
 *
 *
 * @author liujianxin
 * @date 2021/4/20 22:39
 */
public class CGLibProxyDemo {
    
    /**
     * 动态生成的类
     *  public class NoInterfaceObj$$EnhancerByCGLIB$$37bd667 extends NoInterfaceObj implements Factory {
     *      // 拦截器
     *      private MethodInterceptor CGLIB$CALLBACK_0;
     *      // 原方法
     *      private static final Method CGLIB$doSomething$0$Method;
     *      // 增强后的代理方法
     *      private static final MethodProxy CGLIB$doSomething$0$Proxy;
     *
     *      public final String doSomething(String var1) {
     *          // 获取当前方法的拦截器
     *         MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
     *         if (var10000 == null) {
     *             CGLIB$BIND_CALLBACKS(this);
     *             var10000 = this.CGLIB$CALLBACK_0;
     *         }
     *          // 如果拦截器不空就执行拦截器
     *         return var10000 != null ? (String)var10000.intercept(this, CGLIB$doSomething$0$Method, new Object[]{var1}, CGLIB$doSomething$0$Proxy) : super.doSomething(var1);
     *     }
     *
     *
     *  }
     *
     */
    @Test
    public void testCGlibProxy() throws IOException {
        // 动态生成的类保存到本地文件中
        // System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "F:\\IOTest");
        Enhancer enhancer =new Enhancer();
        enhancer.setSuperclass(NoInterfaceObj.class);
        enhancer.setCallback((MethodInterceptor)(enhancerByCGLIBInstance,originalMethod,args,proxiedMethod) ->{
            System.out.println("CGLib执行前...");
            // System.out.println(obj);
            System.out.println(originalMethod);
            System.out.println(Arrays.asList(args));
            System.out.println(proxiedMethod);
            
            
            // 不要用invoke,用invokeSuper
            Object returnValue = proxiedMethod.invokeSuper(enhancerByCGLIBInstance, args);
            System.out.println("CGLib执行后...");
            return returnValue;
        });
        NoInterfaceObj proxied = (NoInterfaceObj)enhancer.create();
        proxied.doSomething("dance");
    
    }
    
   
}
