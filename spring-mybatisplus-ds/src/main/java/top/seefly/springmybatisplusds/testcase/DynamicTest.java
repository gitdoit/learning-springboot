package top.seefly.springmybatisplusds.testcase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.seefly.springmybatisplusds.entity.DeptEntity;
import top.seefly.springmybatisplusds.service.DeptService;

/**
 * @author liujianxin
 * @date 2020/3/5 13:48
 */
public class DynamicTest extends BaseTest {
    @Autowired
    private DeptService deptService;

    /**
     * 演示使用多数据源
     * 可以在 *ServiceImpl/*Mapper 的方法上或者类上添加@DS("db1")注解来
     * 来表示当前方法或者类用哪一个数据源
     *
     * 注意这个注解和AOP事务存在同样的问题
     * 就是方法内部再调用同类下的其他方法，其他方法的事务不生效；
     * 解决方法也是一样，从容器中通过类型获取当前类的实例，然后再调用就可以
     */
    @Test
    public void testInsert2BD(){
        DeptEntity entity = new DeptEntity();
        entity.setDbSource("01");
        entity.setDname("向db1插入数据");
        //deptService.insertDB1(entity);


        DeptEntity entity2 = new DeptEntity();
        entity2.setDbSource("01");
        entity2.setDname("向db2插入数据");
        deptService.insertDB2(entity2);
    }

    /**
     * 测试一下AR模式能不能用动态数据源
     *
     * 可以！ 直接在Entity的方法上使用@DS注解
     */
    @Test
    public void testArAndDS(){
        DeptEntity entity = new DeptEntity().selectFromDB1("开发部");
        System.out.println(entity);
    }


}
