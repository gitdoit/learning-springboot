package org.seefly.springannotation.imports;

import org.seefly.springannotation.imports.selector.MyImportSelector;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author liujianxin
 * @date 2021/4/6 20:25
 */
public class ImportApplicationDemoRun {
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ImportConfig.class);
    
        MyImportSelector.SelectImportsBeanBar bean = context.getBean(MyImportSelector.SelectImportsBeanBar.class);
        System.out.println(bean);
    
    }
}
