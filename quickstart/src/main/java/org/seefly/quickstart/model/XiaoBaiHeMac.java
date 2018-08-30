package org.seefly.quickstart.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author 邵益炯
 * @date 2018/8/9
 */
@Data
@AllArgsConstructor
public class XiaoBaiHeMac {
    private String devmac;
    private String climac;
    private String ssid;
    private String bssid;
    private Date time;
    private Integer signal;
}

