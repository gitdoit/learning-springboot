## Spring

### 1、Spirng Ioc

[IOC概念](https://www.zhihu.com/question/23277575/answer/169698662)

[SpringIOC源码分析](https://javadoop.com/post/spring-ioc)

​	IOC是一种设计思想，就是将原本需要手动管理的对象之间的依赖关系交给Spring来做，这样就解耦了对象之间的强依赖。Spring中的IOC容器就是IOC设计思想的实现，这个容器其实就是一个Map，里面存放各种对象。当我们需要创建一个对象的时候不必手动new，只需要使用配置文件或者注解告诉Spring就行，它会帮我们创建以及注入，我们完全不需要关心这个对象是如何创建的，以及创建它需要什么条件。

### 2、Spring AOP

​	[SpringAOP使用](https://javadoop.com/post/spring-aop-intro)

​	[Aspectj使用](https://www.javadoop.com/post/aspectj)

​	面向切面编程，给我们一种新的维度来编写代码。这种编程方式能够将一些公共的逻辑抽取出来，或者在不变更老的代码的情况下加入新的逻辑。最常用来做一些日志、权限、参数校验等。

​	SpringAOP是基于动态代理的，如果被代理对象实现了接口，那么SpringAOP就会选择JDK Proxy来生成代理对象。如果没有实现接口的话那就使用CGLib来创建代理对象。

​	AspectJ AOP：这是编译时增强，就是在编译阶段生成代理对象。由于是静态的AOP所以效率上会比Spring的动态代理高一些。