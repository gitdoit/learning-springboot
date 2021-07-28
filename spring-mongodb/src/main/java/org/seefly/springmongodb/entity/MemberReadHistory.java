package org.seefly.springmongodb.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@ToString
public class MemberReadHistory {
    
    /**
     * mongodb 要求每个document都要有一个_id
     * 在spring中使用@Id注解标记id字段
     * 如果没有字段被id标记,那么有个名为id的字段也会被作为_id域
     * 如果也没有名为id的字段,那么会mongodb会自己生成一个隐藏的_id域,并且在java中没有对应
     */
    @Id
    private String id;
    
    
    @Indexed
    private Long memberId;
    
    private String memberNickname;
    
    private String memberIcon;
    
    
    @Indexed
    private Long productId;
    
    
    private String productName;
    
    
    private String productPic;
    
    
    private String productSubTitle;
    
    
    private String productPrice;
    
    
    private Date createTime;

    @Version
    private Integer version;
    

    
}
