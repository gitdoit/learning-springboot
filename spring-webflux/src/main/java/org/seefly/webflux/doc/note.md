## 响应式编程笔记
### 教程
>[reactive-streams规范](https://github.com/reactive-streams/reactive-streams-jvm)
> 
>[Project Reactor教程](https://projectreactor.io/docs/core/release/reference/#getting-started) 
>
>[WebFlux教程](https://spring.io/guides/gs/reactive-rest-service/)
>
> [一文读懂响应式编程到底是什么](https://developer.51cto.com/art/202010/629877.htm)
## 体系介绍
### RxJava和Project Reactor以及Spring webflux有什么区别和联系？
RxJava和Project Reactor是基于一套 [响应式编程规范](https://en.wikipedia.org/wiki/Reactive_Streams) 来的，然后在此规范上自己
进行了实现和扩充
 1. RxJava，由奈飞搞的，支持java1.6。主要用在安卓开发上面
 2. Project Reactor，由Spring程序员做的，支持java8及以上
 3. Spring Webflux又是基于 Project Reactor来搞的，都是一家人所以完美适配，相当于响应式版的Spring MVC?不知道这么解释对不对。另外
    Spring cloud gateway又是基于 spring webflux来的。
    

    +--------------------------+     +-------------+     +------------------+     +-------------------------------+
    | Reactive Extensions (Rx) |     | RxJava 1.x  |     | Reactive Streams |     | RxJava 2                      |
    | by Microsoft             +-----> by Netflix  +-----> Specification    +-----> (Supporting Reactive Streams) |
    | for .NET                 |     | for Java 6+ |     | for JVM          |     | for Java 6+                   |
    +--------------------------+     +-------------+     +------------------+     +-------------------------------+
    |
    |
    |
    +-----------------------+     +--------------------+     +---------------v---------------+
    | Java 9 Standard       |     | Spring Framework 5 |     | Project Reactor              |
    | (JEP-266 by Doug Lea) <-----+ Reactive Stack     <-----+ (Supporting Reactive Streams) |
    |                       |     |                    |     | for Java 8+                   |
    +-----------------------+     +--------------------+     +-------------------------------+
