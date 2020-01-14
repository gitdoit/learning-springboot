package org.seefly.springweb.controller.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liujianxin
 * @date 2020/1/14 9:30
 */
@Setter
@Getter
public class SingleChoiceQuestion extends AbstractQuestion {

    private String answer;

    public SingleChoiceQuestion() {
        super(AbstractQuestion.SINGLE_CHOICE);
    }
}
