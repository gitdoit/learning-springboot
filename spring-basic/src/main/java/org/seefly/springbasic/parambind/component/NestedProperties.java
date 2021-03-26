package org.seefly.springbasic.parambind.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

/**
 * yaml 复杂的嵌套数据
 * list嵌套map
 * map嵌套list
 * @author liujianxin
 * @date 2021/3/24 16:14
 */
@Component
@ConfigurationProperties(prefix = "nested")
public class NestedProperties {
    private Map<String, Set<String>> setInMap;
    
    @PostConstruct
    public void after(){
        System.out.println(this.setInMap);
    }
    
    
    
    public Map<String, Set<String>> getSetInMap() {
        return setInMap;
    }
    
    public void setSetInMap(Map<String, Set<String>> setInMap) {
        this.setInMap = setInMap;
    }
    
}
