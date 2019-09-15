package org.seefly.springannotation.entity.autowired;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liujianxin
 * @date 2019/9/15 16:04
 */

@Data
@Component
public class Zoo {
    @Resource(name = "dog")
    private  Animal dog;




    public void setDog(Animal doga){
        this.dog = dog;
    }

}
