package org.seefly.springmongodb.utils;

import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author liujianxin
 * @date 2021/7/16 10:30
 **/
public class MongoClientUtil {

    public static MongoTemplate create(String db){
        String host = System.getenv("MY_SERVER");
        String pwd = System.getenv("MY_PWD");
        return new MongoTemplate(MongoClients.create("mongodb://admin:"+ pwd+"@"+host+":27017/"+db+"?authSource=admin"), db);
    }
}
