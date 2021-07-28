# Spring AOP
> 分为两个模块
>
> 1、basic模块带你一步步的了解Spring AOP为什么是这个样子，了解他的实现思路，有助于我们学习他的原理以及避免一些坑！
>
> 2、Spring AOP的实际应用

## Basic-基础模块

> 本模块讲述java中的代理模式实现以及spring aop的演进；
>
> **代理模式**：目标对象提供一个代理对象，通过访问代理对象来控制对目标对象的访问。通过这种方式可以方便的实现原有业务的扩展，以及避免了直接访问原有对象的复杂性。

### 1、prox包--Java中的代理方式

#### 1.1 静态代理

  所谓静态代理就是代理对象和被代理对象实现同一个接口，并且他们之间的**代理关系在程序运行前就已经确定**了。

  最简单的实现静态代理的方式就是像下面的代码那样手动编码。缺点非常明显，这样的做法**违反了开闭原则**(对扩展开放，对修改关闭)，如果有新的业务功能需要增加，例如发邮件 那么只能再继续实现发邮件接口。

  另外，可以使用[AspectJ](https://www.javadoop.com/post/aspectj)来实现静态编译，这是一种完善的aop解决方案；

```java
public class StaticProxyDemo implements Call {
    private final Call realCall;
    
    public StaticProxyDemo(Call realCall) {
        this.realCall = realCall;
    }
    
    @Override
    public void call(String phone) {
        before();
        realCall.call(phone);
        after();
    }
    
}
```



#### 1.2 动态代理

动态代理就是在程序运行期间，根据运行结果动态的代理某些类，并在内存中生成字节码文件实现代理功能。代理类和被代理类的关系在运行期间才能确定。具体的实现方式有两种。

##### JDK动态代理

> 利用JDK提供的`java.lang.reflect.Proxy`工具类，需要被代理对象提供接口

```java
 public static void main(String[] arg) {
     CallImpl originalObject = new CallImpl();
     // jkd代理
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


// 上面的操作实际在内存中生成如下字节码
public final class $Proxy1 extends Proxy implements Call
  //在内存中生成的class会有一个这样的构造函数，并且在Proxy.newProxyInstance(..)可以看到
  //我们传进去的InvocationHandle会被当作构造参数来构建这个动态生成的类
  public $Proxy1(InvocationHandler var1) throws  {
        super(var1);
    }
  // 然后我们在调用动态生成的类的方法时会执行InvocationHandle.invoke(..)
  public final void call(String var1) throws  {
      try {
          super.h.invoke(this, m3, new Object[]{var1});
      } catch (RuntimeException | Error var3) {
          throw var3;
      } catch (Throwable var4) {
          throw new UndeclaredThrowableException(var4);
      }
  }
}
```



##### CGLib动态代理

> [cglib](https://link.segmentfault.com/?url=https%3A%2F%2Fgithub.com%2Fcglib%2Fcglib) (Code Generation Library )是一个第三方代码生成类库，运行时在内存中动态生成一个子类对象从而实现对目标对象功能的扩展;
>
> 在Spring中对于cglib的依赖已经包含在了`spring-core`模块里面。

```java
public static void main(String arg) {
    Enhancer enhancer =new Enhancer();
    // 被代理类
    enhancer.setSuperclass(NoInterfaceObj.class);
    // 增强逻辑
    enhancer.setCallback((MethodInterceptor)(enhancerByCGLIBInstance,originalMethod,args,proxiedMethod) ->{
        System.out.println("CGLib执行前...");
        // 不要用invoke,用invokeSuper
        Object returnValue = proxiedMethod.invokeSuper(enhancerByCGLIBInstance, args);
        System.out.println("CGLib执行后...");
        return returnValue;
    });
    // 创建实例
    NoInterfaceObj proxied = (NoInterfaceObj)enhancer.create();
    proxied.doSomething("dance");
}

// 在内存中生成的被代理类
public class NoInterfaceObj$$EnhancerByCGLIB$$37bd667 extends NoInterfaceObj implements Factory {
    // 拦截器
    private MethodInterceptor CGLIB$CALLBACK_0;
    // 原方法
    private static final Method CGLIB$doSomething$0$Method;
    // 增强后的代理方法
    private static final MethodProxy CGLIB$doSomething$0$Proxy;
    public final String doSomething(String var1) {
        // 获取当前方法的拦截器
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (var10000 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }
        // 如果拦截器不空就执行拦截器
        return var10000 != null ? (String)var10000.intercept(this, CGLIB$doSomething$0$Method, new Object[]{var1}, CGLIB$doSomething$0$Proxy) : super.doSomething(var1);
    }
}
```





### 2、evolution包-Sping中AOP的演进

> [Spring AOP 使用介绍，从前世到今生](https://javadoop.com/post/spring-aop-intro)
>
> 演示Spring AOP的发展历程





## Spring-aop在spring中的使用

1、基本的切面定义方式

2、private方法代理失效的原因

3、注解拦截和集成SpEl表达式

