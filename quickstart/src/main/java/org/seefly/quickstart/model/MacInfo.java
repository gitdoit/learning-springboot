package org.seefly.quickstart.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujianxin
 * @date 2018/8/6 16:34
 * 描述：
 */
@TableName("wifi_zhongke")
@Data
public class MacInfo extends Model<MacInfo> {


    private String id;

    /**
     * 设备MAC
     */
    private String device;
    /**
     * 源MAC
     */
    private String source;
    /**
     * 目标MAC
     */
    private String router;

    @TableField(exist = false)
    private boolean isInserted = false;

    public boolean getIsInserted(){
        return this.isInserted;
    }



    public Object[] toList(){
        Object[] list = new Object[5];
        list[0] = this.device;
        list[1] = (this.source);
        list[2] = this.router;
        return list;
    }

    public void setSource(String source){
        this.source = source;
        this.id = "0809"+source;
    }





    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
