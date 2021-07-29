package org.seefly.springmongodb.projections;

/**
 * @author liujianxin
 * @date 2021/7/29 17:51
 **/
public interface DynamicProjections {
    
    interface OnlyName {
        String getName();
    }
    
    interface OnlyAge {
        Integer getAge();
    }
    
    interface NameAndAge{
        String getName();
        Integer getAge();
    }
    
}
