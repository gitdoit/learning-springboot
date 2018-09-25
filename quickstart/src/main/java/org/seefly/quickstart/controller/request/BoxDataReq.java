package org.seefly.quickstart.controller.request;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author liujianxin
 * 天津子午线探针数据上传
 * 只处理指定字段：dmac mac model stime etime
 */
@Data
public class BoxDataReq {
    private List<BoxData> data;



    @Data
    public static class BoxData{
        /**
         * 序号，无意义
         */
        @CsvBindByPosition(position = 0)
        private String id;
        /**
         * 被探测设备MAC
         */
        @CsvBindByPosition(position = 1)
        private String mac;
        /**
         * 被探测设备的类型 (1 表示被探测设备是一个无线网卡或者手机.)
         */
        @CsvBindByPosition(position = 2)
        private String model;

        /**
         * 被探测设备的信号强度
         */
        @CsvBindByPosition(position = 3)
        private String rssi;
        /**
         * 被探测设备信道
         */
        @CsvBindByPosition(position = 4)
        private String channel;
        /**
         * 被探测设备的报文类型
         */
        @CsvBindByPosition(position = 5)
        private String pkttype;

        /**
         * 含义类似,放在一起,每个值是一个计数,根据扫描到的信息进行记录
         */
        @CsvBindByPosition(position = 6)
        private String fmgt;
        @CsvBindByPosition(position = 7)
        private String fctl;
        @CsvBindByPosition(position = 8)
        private String fdata;
        @CsvBindByPosition(position = 9)
        private String fmagic;
        /**
         * 记录开始的时间
         */
        @CsvBindByPosition(position = 10)
        private String stime;
        /**
         * 记录结束的时间
         */
        @CsvBindByPosition(position = 11)
        private String eitme;
        /**
         * 记录开始设备上的运行时间，暂时忽略
         */
        @CsvBindByPosition(position = 12)
        private String slocal;
        /**
         * 记录结束设备上的运行时间，暂时忽略
         */
        @CsvBindByPosition(position = 13)
        private String elocal;
        /**
         * 探针MAC
         */
        @CsvBindByPosition(position = 14)
        private String dmac;

        /**
         * 重写equals、hashCode用于同一批数据去重
         * @param o 参数
         * @return mac和dmac都相等 true 不等false
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()){
                return false;
            }
            BoxData boxData = (BoxData) o;
            return Objects.equals(mac, boxData.mac) &&
                    Objects.equals(dmac, boxData.dmac);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mac, dmac);
        }
    }



}
