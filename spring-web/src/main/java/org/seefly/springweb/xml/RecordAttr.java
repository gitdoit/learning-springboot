package org.seefly.springweb.xml;

import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author liujianxin
 * @date 2019/11/20 9:51
 */
@javax.xml.bind.annotation.XmlRootElement(name="Record")
@Setter
public class RecordAttr {

    /**
     * 档案号 = 全宗号代码-分类号(ZG).目录号-保存期限（Y）-业务类型.机构代码-案卷号
     * demo: 0103-ZG.2019-Y-ZH.0706-003-12
     */
    /**
     * 档案编号
     */
    @XmlElement(name = "RECORD_NUMBER")
    private String recordNumber;

    /**档案号*/
    @XmlElement(name = "ARCHIVAL_CODE")
    private String archivalCode;

    /**全宗号-乡镇枚举编号*/
    @XmlElement(name = "FONDS_NO")
    private String fondsNo;
    /**分类号-ZG*/
    @XmlElement(name = "CLASS_CODE")
    private String classCode;
    /**目录号-同形成年度,如 2019*/
    @XmlElement(name = "CATALOGUE_CODE")
    private String catalogueCode;
    /**保存期限-Y*/
    @XmlElement(name = "RETENTION_PERIOD")
    private String retentionPeriod;
    /**业务类型-业务类型枚举值*/
    @XmlElement(name = "YWLX")
    private String ywlx;
    /**机构代码-机构枚举值*/
    @XmlElement(name = "JGDM")
    private String jgdm;
    /**案卷号-若存在一条记录和当前记录在以上字段值重复时，这里顺延+1。保证档案号唯一*/
    @XmlElement(name = "FILE_CODE")
    private String fileCode;
    /**案卷支号，同上*/
    @XmlElement(name = "SUB_CODE")
    private String subCode;

    /**
     * 生成档案号
     * demo : 0103-ZG.2019-Y-ZH.0706-003-12
     */
    public String generateArchivalCode(){
        StringBuilder sb = new StringBuilder();
        sb.append(fondsNo).append("-")
                .append(classCode).append(".")
                .append(catalogueCode).append("-")
                .append(retentionPeriod).append("-")
                .append(ywlx).append(".")
                .append(jgdm).append("-")
                .append(fileCode.length() == 1 ? "00" + fileCode : (fileCode.length() == 2 ? "0" + fileCode : fileCode));
        if(!StringUtils.isEmpty(subCode)){
            sb.append("-").append(subCode);
        }
        return this.archivalCode = sb.toString();
    }


    /**形成年度*/
    @XmlElement(name = "FORM_YEAR")
    private String formYear;
    /**jgdm对应的中文乡镇村*/
    @XmlElement(name = "DWMC")
    private String dwmc;
    /** 退出类型-枚举*/
    @XmlElement(name = "LX")
    private String lx;


    /**件数*/
    @XmlElement(name = "DOCUMENT_AMOUNT")
    private String documentAmount;
    /**页数*/
    @XmlElement(name = "PAGE_AMOUNT")
    private String pageAmount;
    /**签出人*/
    @XmlElement(name = "CHECKOUT_BY")
    private String checkoutBy;
    /**签出时间*/
    @XmlElement(name = "CHECKOUT_TIME")
    private String checkoutTime;
    /**创建人员*/
    @XmlElement(name = "CREATED_BY")
    private String createdBy;
    /**创建时间*/
    @XmlElement(name = "CREATED_TIME")
    private String createdTime;
    /**修改人员*/
    @XmlElement(name = "MODIFIED_BY")
    private String modifiedBy;
    /**修改时间*/
    @XmlElement(name = "MODIFIED_TIME")
    private String modifiedTime;
    /**第一读取存储设备*/
    @XmlElement(name = "STORAGE_ID")
    private String storageId;
    /**是否存在影像*/
    @XmlElement(name = "EXIST_PAGE")
    private String existPage;
    /**状态*/
    @XmlElement(name = "STATE")
    private String state;
    /**权利人*/
    @XmlElement(name = "QLR")
    private String qlr;
    /**同案卷号-file_code*/
    @XmlElement(name = "XAJH")
    private String xajh;



    /**保密等级*/
    @XmlElement(name = "SECURITY_LEVEL")
    private String security_level;
    /**起始时间*/
    @XmlElement(name = "START_DATE")
    private String startDate;
    /**结束时间*/
    @XmlElement(name = "END_DATE")
    private String endDate;

    /**备注*/
    @XmlElement(name = "MEMO")
    private String memo;
    /**立卷时间*/
    @XmlElement(name = "FILING_DATE")
    private String filingDate;
    /**立卷人*/
    @XmlElement(name = "filing_by")
    private String filingBy;
    /**检查人*/
    @XmlElement(name = "CHECKED_BY")
    private String checkedBy;
    /**卷内情况说明*/
    @XmlElement(name = "DESCRIPTION")
    private String description;

    /**？？？*/
    @XmlElement(name = "MLH")
    private String mlh;

    /**？？？*/
    @XmlElement(name = "JGQC")
    private String jgqc;
    /**？？？*/
    @XmlElement(name = "JGQC2")
    private String jgqc2;
    /**？？*/
    @XmlElement(name = "IDBAK")
    private String idbak;
    /**乡镇*/
    @XmlElement(name = "DWMC_X")
    private String dwmcX;
    /**村*/
    @XmlElement(name = "DWMC_C")
    private String dwmcC;



}
