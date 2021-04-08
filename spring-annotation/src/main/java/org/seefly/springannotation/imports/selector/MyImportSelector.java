package org.seefly.springannotation.imports.selector;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 配合{@link Import}注解，向容器中批量导入组件
 * 根据返回值表明的类的全限定名，将决定向容器中注入哪些Bean
 *
 * 这种方式向容器中注入的bean，只能按类型查找---> applicationContext.getBean(xxx.class);不能applicationContext.getBean('beanName')
 * @author liujianxin
 * @date 2018-12-23 16:25
 */
public class MyImportSelector implements ImportSelector {
    
    public static void main(String[] args) {
    
    }
    
    

    /**
     *
     * @param importingClassMetadata 引用方的注解信息
     * @return 需要向容器中添加的组件的全限定名，不应该返回null
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                "org.seefly.springannotation.imports.selector.MyImportSelector.SelectImportsBeanFoo"
                ,"org.seefly.springannotation.imports.selector.MyImportSelector.SelectImportsBeanBar"
            };
    }
    
    
    
    
    public static class SelectImportsBeanFoo{
    
        public SelectImportsBeanFoo() {
            System.out.println("@import+ImportSelector接口注入：SelectImportsBeanFoo");
        }
    }
    
    public static class SelectImportsBeanBar{
    
        public SelectImportsBeanBar() {
            System.out.println("@import+ImportSelector接口注入：SelectImportsBeanBar");
        }
    }
}
