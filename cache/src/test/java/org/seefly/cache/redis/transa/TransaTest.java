package org.seefly.cache.redis.transa;

import org.junit.Test;
import org.seefly.cache.redis.baseops.BaseOps;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author liujianxin
 * @date 2018-11-01 18:52
 */
public class TransaTest extends BaseOps {

    /**
     * 基本事务演示
     */
    @Test
    public void testTrans(){
        stringTemplate.setEnableTransactionSupport(true);
        List<Object> execute = stringTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations ops) throws DataAccessException {
                //开启一个事务，必须在exec之前使用
                stringTemplate.multi();
                //这三条命令并不是批量发送过去，也是一次请求一次响应
                stringTemplate.boundValueOps("trans:key1").set("aaaaaaaaaaa");
                //stringTemplate.boundValueOps("trans:key1").increment(1L);
                stringTemplate.boundValueOps("trans:key2").set("bbbbbbbbbbbbbbbbbbbbbbb");
                //提交事务，返回执行结果
                return stringTemplate.exec();
            }
        });
        //返回命令执行的结果
        System.out.println(execute.size());
    }

    /**
     * discard是撤销命令队列中还未执行的命令
     * 它并不能回滚已经执行的操作
     */
    @Test
    public void testTrans1(){
        List<Object> execute = stringTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations ops) throws DataAccessException {
                stringTemplate.multi();
                try{
                    stringTemplate.boundValueOps("trans:key3").set("cccccccccc");
                    //模拟代码异常
                    int i = 1 / 0;
                    //对于命令上的错误，例如试图给字符串自增，discard命令是不起作用的。因为命令队列中的操作已经被执行了才知道你的命令有误
                    //这时候命令队列
                    //stringTemplate.boundValueOps("trans:key3").increment(1L);
                    stringTemplate.boundValueOps("trans:key4").set("cccccccccc");
                }catch (Exception e){
                    stringTemplate.discard();
                }
                return stringTemplate.exec();
            }
        });
        //返回命令执行的结果
        System.out.println(execute.size());
    }



    /**
     * spring对redis的事务也有支持
     * 直接使用@Transactional注解就可以了
     * 但不管成功还是失败，单元测试中的事务，都不会提交
     *
     */
    @Transactional
    @Commit
    @Test
    public void testTrans2(){
        BoundValueOperations<String, String> user1 = stringTemplate.boundValueOps("trans:user1");
        BoundValueOperations<String, String> user2 = stringTemplate.boundValueOps("trans:user2");
        // 更新操作，使用@Transactional注解并不会立即执行
        user1.set("500");
        user2.set("500");
        //模拟异常
        int i = 1 / 0;

    }


    /**
     * 乐观锁：watch
     * 在事务开始之前使用watch命令盯住一个或多个键
     * 如果事务开始执行的时候，这些键都没有发生改变的话就去执行事务
     * 如果发生了改变，则执行失败
     */
    @Test
    public void testWatch(){
        BoundValueOperations<String, String> user1 = stringTemplate.boundValueOps("watch:user1");
        BoundValueOperations<String, String> user2 = stringTemplate.boundValueOps("watch:user2");
        //开始一人500块
        user1.set("500");
        user2.set("500");
        stringTemplate.setEnableTransactionSupport(true);
        List<Object> execute = stringTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations ops) throws DataAccessException {
                stringTemplate.watch(Arrays.asList("watch:user1","watch:user2"));
                //开启事务
                stringTemplate.multi();
                //转账
                user1.increment(-500);
                user2.increment(500);
                //提交事务
                return stringTemplate.exec();
            }
        });
        //打印命令执行结果
        for(Object order : execute){
            System.out.println(order);
        }
    }

    @Test
    public void testWatch1(){
        BoundValueOperations<String, String> user1 = stringTemplate.boundValueOps("watch:user1");
        BoundValueOperations<String, String> user2 = stringTemplate.boundValueOps("watch:user2");
        //开始一人500块
        user1.set("500");
        user2.set("500");
        stringTemplate.setEnableTransactionSupport(true);
        List<Object> execute = stringTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations ops) throws DataAccessException {
                stringTemplate.watch(Arrays.asList("watch:user1","watch:user2"));
                //模拟其他客户端更新命令
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //开启事务
                stringTemplate.multi();
                //转账
                user1.increment(-500);
                user2.increment(500);
                //提交事务
                return stringTemplate.exec();
            }
        });
        if(CollectionUtils.isEmpty(execute)){
            System.out.println("事务执行失败！");
        }else {
            //打印命令执行结果
            for(Object order : execute){
                System.out.println(order);
            }
        }
    }

}
