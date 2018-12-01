package org.seefly.quickstart;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author liujianxin
 * @date 2018-06-16 21:35
 * 描述信息：
 **/
public class ControllerTest {
    private MockMvc mockMvc;

    //初始化工作



    //测试
    @Test
    public void index() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/index")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
