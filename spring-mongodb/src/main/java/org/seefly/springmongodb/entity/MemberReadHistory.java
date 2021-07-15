package org.seefly.springmongodb.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@ToString
public class MemberReadHistory {
    
    
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
    
    //省略了所有getter和setter方法
    
    
}
