package org.seefly.springweb.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author liujianxin
 * @date 2020/1/14 9:35
 */
@Setter
@Getter
public class MultipleChoiceQuestion extends AbstractQuestion {
    private List<String> answers;

    public MultipleChoiceQuestion() {
        super(AbstractQuestion.MULTIPLE_CHOICE);
    }
}
