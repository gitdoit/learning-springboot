package org.seefly.mybatisplus.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liujianxin
 * @date 2018/8/6 16:34
 * 描述：
 */
@TableName("wifi_zhongke")
@Data
public class MacInfo extends Model<MacInfo> {


    private static final SimpleDateFormat  fmt = new SimpleDateFormat("MMdd");


    private String id;

    /**
     * 设备MAC
     */
    private String deviceMac;
    /**
     * 源MAC
     */
    private String sourceMac;
    /**
     * 目标MAC
     */
    private String targetMac;
    /**
     * 主帧类型
     */
    private String mainFrameType;
    /**
     * 子帧类型
     */
    private String chileFrameType;
    /**
     * 信道
     */
    private String channel;
    /**
     * 信号强度
     */
    private String signalIntensity;
    /**
     * 是否为省电模式
     */
    private String isSavePowerModel;
    /**
     * 数据是否来非自路由器
     */
    private String isNotFromRouter;

    /**
     * 起始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    public Object[] toList(){
        Object[] list = new Object[5];
        list[0] = this.getDeviceMac();
        list[1] = (this.sourceMac);
        list[2] = (this.targetMac);
        list[3] = (this.startTime);
        list[4] = (this.endTime);
        return list;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void setSourceMac(String sourceMac){
        this.sourceMac = sourceMac;
        this.id = sourceMac.replace(":","").concat(fmt.format(new Date()));
    }
}
