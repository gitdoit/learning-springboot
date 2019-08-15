package org.seefly.jvm.loadclass;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 详细的 https://blog.csdn.net/djokermax/article/details/81539639
 *
 * 三种类加载器
 * 1、启动类加载器： BootstrapClassLoader
 *      负责加载 ${JAVA_HOME}/jre/lib 文件夹下的类库
 * 2、扩展类加载器： ExtensionClassLoader
 *      负责加载 ${JAVA_HOME}/jre/lib/ext 文件夹下的类库
 * 3、应用程序类加载器： ApplicationClassLoader
 *      该类加载器由 sun.misc.Launcher$AppClassLoader来实现
 *      它负责加载用户类路径（ClassPath）所指定的类，开发者可以直接使用该类加载器
 *      如果应用程序中没有自定义过自己的类加载器，一般情况下这个就是程序中默认的类加载器。
 *
 *
 * 双亲委派机制。
 * 听着很高大上，其实很简单。比如A类的加载器是AppClassLoader(其实我们自己写的类的加载器都是AppClassLoader)，
 * AppClassLoader不会自己去加载类，而会委ExtClassLoader进行加载，那么到了ExtClassLoader类加载器的时候，
 * 它也不会自己去加载，而是委托BootStrap类加载器进行加载，就这样一层一层往上委托，如果Bootstrap类加载器无法进行加载的话，再一层层往下走。
 *
 * 总的来说，在加载类的时候先不要自己加载，调用父加载器去加载，父类也是同样的逻辑，直到父类为空的时候
 * 那时候就直接调用BootStrapClassLoader了，如果没有加载到，儿子再加载，孙子在加载...
 * 这种模式的好处就是保证了加载到的类是安全的，不是被篡改过的，像是lang包下的都是由根加载器去加载的，避免了
 * 随便一个类加载器都能替换String
 *
 * @author liujianxin
 * @date 2019-08-15 09:55
 */
public class MyClassLoader {

    /**
     * 看看classLoader
     */
    @Test
    public void testShowClassLoader(){
        // 为啥class点一下都是getClassLoader，这个咋就那么特别非要叫上下文类加载器？
        // 注释说，默认的就是AppClassLoader
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        // sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(contextClassLoader);
        // sun.misc.Launcher$ExtClassLoader@2f2c9b19
        System.out.println(contextClassLoader.getParent());
        // null 也就是BootStrapClassLoader，C写的，java拿不到
        System.out.println(contextClassLoader.getParent().getParent());
    }



    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 这个和当前主题无关，我就是觉得牛逼，记录一下
        //Class<?> caller = Reflection.getCallerClass();

        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                // getResourceAsStream没有以 / 开头，即获取当前类同目录下的资源
                InputStream resourceAsStream = getClass().getResourceAsStream(name.substring(name.lastIndexOf(".")+1)+".class");
                if(resourceAsStream == null){
                    return super.loadClass(name);
                }
                try {
                    byte[] bytes = new byte[resourceAsStream.available()];
                    resourceAsStream.read(bytes);
                    // 字节转class
                    return defineClass(name,bytes, 0, bytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return super.loadClass(name);
            }
        };
        // 咱们自己的类加载器加载的类，让他创建一个实例
        Object classLoadDemo = classLoader.loadClass("org.seefly.jvm.loadclass.MyClassLoader").newInstance();
        // 这个实例按理来说就是ClassLoadDemo类型，但是打印的是false
        // 因为就算是同一个class，如果使用不同的类加载器去加载，它俩也不是一样的。
        // 由于ClassLoadDemo是由AppClassLoader去加载的，而咱们创建的实例是由自己的类加载器加载过来的，所以不一样
        System.out.println(classLoadDemo instanceof MyClassLoader);


        /**
         * org.seefly.jvm.loadclass.MyClassLoader$1@4d7e1886
         * sun.misc.Launcher$AppClassLoader@18b4aac2
         * sun.misc.Launcher$ExtClassLoader@7440e464
         */
        ClassLoader cl = classLoadDemo.getClass().getClassLoader();
        while (cl != null){
            System.out.println(cl);
            cl = cl.getParent();
        }
    }
}
