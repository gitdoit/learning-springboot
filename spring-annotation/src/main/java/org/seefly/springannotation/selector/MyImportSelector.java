package org.seefly.springannotation.selector;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 配合{@link Import}注解，向容器中批量导入组件
 * @author liujianxin
 * @date 2018-12-23 16:25
 */
public class MyImportSelector implements ImportSelector {

    /**
     *
     * @param importingClassMetadata 引用方的注解信息
     * @return 需要向容器中添加的组件的全限定名，不应该返回null
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"org.seefly.springannotation.entity.ImportSelectorBean","org.seefly.springannotation.entity.ImportSelectorBean1"};
    }
}
