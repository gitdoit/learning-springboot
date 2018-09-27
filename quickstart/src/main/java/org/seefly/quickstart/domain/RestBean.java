package org.seefly.quickstart.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author liujianxin
 * @date 2018-07-13 17:16
 * 描述信息：
 **/
@XmlRootElement(name="restBean")
public class RestBean {
    @XmlElement
    private String name = "aaa";
    @XmlElement
    private String age = "bbb";

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }
}
