package org.seefly.springannotation;

import org.junit.Before;
import org.junit.Test;
import org.seefly.springannotation.config.AutowiredConfig;
import org.seefly.springannotation.entity.autowired.Zoo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 关于容器中的依赖注入
 * 如果使用@Autowired注解，不论是把它引用在构造方法、Field、或者是set方法上
 * 它都是先按照类型来从容器中获取需要的实例，如果没有，默认抛出异常
 * 如果拿到了多个，那么会按照名称来进行匹配，匹配成功则注入，否则抛出异常告诉你需要一个，但找到了两个
 * 另外如果你的容器中有多个相同类型的Bean的话，那么可以使用Primary注解来标识哪个是主要的
 *
 * @author liujianxin
 * @date 2019/9/15 16:12
 */

public class AutowiredTest extends BaseTest {
    private ApplicationContext applicationContext;

    @Before
    public void before(){
        applicationContext = new AnnotationConfigApplicationContext(AutowiredConfig.class);
        System.out.println("容器初始化完毕...");
    }

    @Test
    public void testGet(){
        Zoo bean = applicationContext.getBean(Zoo.class);
        System.out.println(bean.getDog());
    }
}
