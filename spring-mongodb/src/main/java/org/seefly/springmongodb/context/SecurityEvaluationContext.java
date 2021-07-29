package org.seefly.springmongodb.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author liujianxin
 * @date 2021/7/29 15:32
 **/
@Component
public class SecurityEvaluationContext implements EvaluationContextExtension {
    
    /**
     * 多个上下文的情况下这个方法的作用类似 namespace
     */
    @Override
    public String getExtensionId() {
        return "security";
    }
    
    
    @Override
    public SecurityExpressionRoot getRootObject() {
        return SecurityExpressionRoot.justGiveMeTheFuckingDefault();
    }
    
    
    /**
     * 不知道为定义在这里的方法也能被调用
     * 但必须是静态的
     */
    public static String staticName(){
        return "Sink_Ring";
    }
    
   
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SecurityExpressionRoot {
        private List<String> argId;
        private String tenantId;
        private String userName;
    
        /**
         * 当然可以通过SpEl调用方法
         */
        public int randomAge(){
            Random random = new Random(121);
            return Math.abs(random.nextInt() % 100);
        }
        
        public static SecurityExpressionRoot justGiveMeTheFuckingDefault(){
            return new SecurityExpressionRoot(Arrays.asList("company_1","company_3"),"tenant_123","Michael jackson");
        }
    }
    
    
}
