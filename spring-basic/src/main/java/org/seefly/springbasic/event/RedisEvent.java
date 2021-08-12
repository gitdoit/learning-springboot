package org.seefly.springbasic.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liujianxin
 * @date 2021/8/10 15:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisEvent {
    private String payload;
}
