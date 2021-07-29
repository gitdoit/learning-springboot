package org.seefly.springmongodb.repository;


/**
 * @author liujianxin
 * @date 2021/7/29 10:19
 **/
public interface CustomizedRepository <T> {
    
    <S extends T> S save(S entity);
    
}
