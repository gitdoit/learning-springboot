package org.seefly.springsecurity.QueryTest;

import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.seefly.springsecurity.BaseTest;
import org.seefly.springsecurity.entity.ClientDetails;
import org.seefly.springsecurity.mapper.ClientDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liujianxin
 * @date 2018-12-24 14:27
 */
public class ClientDetailsTest  extends BaseTest {
    @Autowired
    private ClientDetailsMapper mapper;


    @Test
    public void testSelectClientDetailsById(){
        ClientDetails client = mapper.findByClientId("client");
        Assert.notNull(client);
    }
}
