package org.seefly.springweb.controller.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * JsonTypeInfo作用域接口或者是类，用来处理多态类型的序列化和反序列化
 *      use: 序列化和反序列化的时候用那种类别区分，name表示使用字段作为序列化依据
 *      property: 配合use使用，指定那个字段
 *      include
 *      visible：关键字段是否参与序列化
 *
 *
 * @author liujianxin
 * @date 2020/1/14 9:26
 */
@Data
@JsonTypeInfo(use =JsonTypeInfo.Id.NAME,
        property = "type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleChoiceQuestion.class, name = AbstractQuestion.SINGLE_CHOICE),
        @JsonSubTypes.Type(value = MultipleChoiceQuestion.class, name = AbstractQuestion.MULTIPLE_CHOICE)
})
public abstract class AbstractQuestion {
    protected static final String SINGLE_CHOICE = "single_choice";
    protected static final String MULTIPLE_CHOICE = "multiple_choice";

    protected String type;
    protected String content;

    public AbstractQuestion(String type) {
        this.type = type;
    }
}


