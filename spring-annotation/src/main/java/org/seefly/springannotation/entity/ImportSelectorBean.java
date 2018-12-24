package org.seefly.springannotation.entity;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;

/**
 * 使用{@link ImportSelector}的自定义实现类，配合{@link Import}，批量导入组件。
 * 传入本类的全限定名，即可添加一个本类实例到容器。
 * @author liujianxin
 * @date 2018-12-23 16:28
 */
public class ImportSelectorBean {
}
