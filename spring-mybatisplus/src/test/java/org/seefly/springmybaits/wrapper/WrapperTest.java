package org.seefly.springmybaits.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.seefly.springmybaits.BaseTest;
import org.seefly.springmybaits.entity.ArUser;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-12-01 21:39
 */
public class WrapperTest extends BaseTest {
    /**
     * AR模式
     */
    @Test
    public void test1(){
        ArUser arUser = new ArUser().selectOne(new QueryWrapper<ArUser>().eq("id",1));
        System.out.println(arUser);
    }

    /**
     * LAMBDA
     */
    @Test
    public void test2(){
        Integer integer = arUserMapper.selectCount(new QueryWrapper<ArUser>().lambda().lt(ArUser::getId, 3));
        System.out.println(integer);
    }

    /**
     * sql注入 + AR
     */
    @Test
    public void test3(){
        // SELECT id,role_id,name,email,age,gender,grade FROM user WHERE role_id IN (select id from role where id = 2)
        List<ArUser> list = arUserMapper.selectList(new QueryWrapper<ArUser>().inSql("role_id", "select id from role where id = 2"));
        List<ArUser> arList = new ArUser().selectList(new QueryWrapper<ArUser>().lambda().inSql(ArUser::getRoleId, "select id from role where id = 2"));
        assert list.size() == arList.size();
    }

    /**
     * 嵌套查询
     */
    @Test
    public void test4(){
        //SELECT id,role_id,name,email,age,gender,grade FROM user WHERE ( role_id = ? ) AND ( age >= ? )
        QueryWrapper<ArUser> query = new QueryWrapper<ArUser>().nested(i -> i.eq("role_id", 2)).and(i ->i.ge("age",20));
        //SELECT id,role_id,name,email,age,gender,grade FROM user WHERE ( role_id = ? OR id IN (?,?,?) )
        LambdaQueryWrapper<ArUser> nested = new QueryWrapper<ArUser>().lambda().nested(i -> i.eq(ArUser::getRoleId, 2).or().in(ArUser::getId, 1, 2, 3));
        List<ArUser> arUsers1 = arUserMapper.selectList(nested);
        List<ArUser> arUsers = arUserMapper.selectList(query);
    }

}
