package com.enterprise.platform.inventorymanagement.model.sqlserver;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "HY_BarCodeMain")
@Data
public class HYBarCodeMain {
    @Id    
    @Column(name = "BarCode", length = 300, nullable = false)
    @JsonProperty("barcode")
    private String barcode;

    @Column(name = "BarCodeRule", length = 20)
    @JsonProperty("barCodeRule")
    private String barCodeRule;

    @Column(name = "cInvCode", length = 60)
    @JsonProperty("cInvCode")
    private String cInvCode;

    @Column(name = "cVenCode", length = 20)
    @JsonProperty("cVenCode")
    private String cVenCode;

    @Column(name = "cWhCode", length = 10)
    @JsonProperty("cWhCode")
    private String cWhCode;

    @Column(name = "cPosCode", length = 20)
    @JsonProperty("cPosCode")
    private String cPosCode;

    @Column(name = "iInvSaleCost")
    @JsonProperty("iInvSaleCost")
    private Double iInvSaleCost;

    @Column(name = "dMdate")
    @JsonProperty("dMdate")
    private Date dMdate;

    @Column(name = "dVdate")
    @JsonProperty("dVdate")
    private Date dVdate;

    @Column(name = "iMassdate")
    @JsonProperty("iMassdate")
    private Integer iMassdate;

    @Column(name = "cMassUnit")
    @JsonProperty("cMassUnit")
    private Short cMassUnit;

    @Column(name = "cChkItemCode", length = 30)
    @JsonProperty("cChkItemCode")
    private String cChkItemCode;

    @Column(name = "cOther", length = 50)
    @JsonProperty("cOther")
    private String cOther;

    @Column(name = "cHoldItem", length = 50)
    @JsonProperty("cHoldItem")
    private String cHoldItem;

    @Column(name = "cSHoldItem", length = 50)
    @JsonProperty("cSHoldItem")
    private String cSHoldItem;

    @Column(name = "cSilvItem", length = 50)
    @JsonProperty("cSilvItem")
    private String cSilvItem;

    @Column(name = "cFree1", length = 40)
    @JsonProperty("cFree1")
    private String cFree1;

    @Column(name = "cFree2", length = 40)
    @JsonProperty("cFree2")
    private String cFree2;

    @Column(name = "cFree3", length = 40)
    @JsonProperty("cFree3")
    private String cFree3;

    @Column(name = "cFree4", length = 40)
    @JsonProperty("cFree4")
    private String cFree4;

    @Column(name = "cFree5", length = 40)
    @JsonProperty("cFree5")
    private String cFree5;

    @Column(name = "cFree6", length = 40)
    @JsonProperty("cFree6")
    private String cFree6;

    @Column(name = "cFree7", length = 40)
    @JsonProperty("cFree7")
    private String cFree7;

    @Column(name = "cFree8", length = 40)
    @JsonProperty("cFree8")
    private String cFree8;

    @Column(name = "cFree9", length = 40)
    @JsonProperty("cFree9")
    private String cFree9;

    @Column(name = "cFree10", length = 40)
    @JsonProperty("cFree10")
    private String cFree10;

    @Column(name = "qty", precision = 38, scale = 6)
    @JsonProperty("qty")
    private java.math.BigDecimal qty;

    @Column(name = "iNum", precision = 38, scale = 6)
    @JsonProperty("iNum")
    private java.math.BigDecimal iNum;

    @Column(name = "pLot", length = 60)
    @JsonProperty("pLot")
    private String pLot;

    @Column(name = "ichangerate", precision = 38, scale = 6)
    @JsonProperty("ichangerate")
    private java.math.BigDecimal ichangerate;

    @Column(name = "cInvSN", length = 30)
    @JsonProperty("cInvSN")
    private String cInvSN;

    @Column(name = "cDefine1", length = 20)
    @JsonProperty("cDefine1")
    private String cDefine1;

    @Column(name = "cDefine2", length = 20)
    @JsonProperty("cDefine2")
    private String cDefine2;

    @Column(name = "cDefine3", length = 20)
    @JsonProperty("cDefine3")
    private String cDefine3;

    @Column(name = "cDefine4")
    @JsonProperty("cDefine4")
    private Date cDefine4;

    @Column(name = "cDefine5")
    @JsonProperty("cDefine5")
    private Integer cDefine5;

    @Column(name = "cDefine6")
    @JsonProperty("cDefine6")
    private Date cDefine6;

    @Column(name = "cDefine7")  
    @JsonProperty("cDefine7")
    private Double cDefine7;

    @Column(name = "cDefine8", length = 4)
    @JsonProperty("cDefine8")
    private String cDefine8;

    @Column(name = "cDefine9", length = 8)
    @JsonProperty("cDefine9")
    private String cDefine9;

    @Column(name = "cDefine10", length = 60)
    @JsonProperty("cDefine10")
    private String cDefine10;

    @Column(name = "cDefine11", length = 120)
    @JsonProperty("cDefine11")
    private String cDefine11;

    @Column(name = "cDefine12", length = 120)
    @JsonProperty("cDefine12")
    private String cDefine12;

    @Column(name = "cDefine13", length = 120)
    @JsonProperty("cDefine13")
    private String cDefine13;

    @Column(name = "cDefine14", length = 120)
    @JsonProperty("cDefine14")
    private String cDefine14;

    @Column(name = "cDefine15")
    @JsonProperty("cDefine15")
    private Integer cDefine15;

    @Column(name = "cDefine16")
    @JsonProperty("cDefine16")
    private Double cDefine16;

    @Column(name = "cDefine22", length = 60)
    @JsonProperty("cDefine22")
    private String cDefine22;

    @Column(name = "cDefine23", length = 60)
    @JsonProperty("cDefine23")
    private String cDefine23;

    @Column(name = "cDefine24", length = 60)
    @JsonProperty("cDefine24")
    private String cDefine24;

    @Column(name = "cDefine25", length = 60)
    @JsonProperty("cDefine25")
    private String cDefine25;

    @Column(name = "cDefine26")
    @JsonProperty("cDefine26")
    private Double cDefine26;

    @Column(name = "cDefine27") 
    @JsonProperty("cDefine27")
    private Double cDefine27;

    @Column(name = "cDefine28", length = 120)
    @JsonProperty("cDefine28")
    private String cDefine28;

    @Column(name = "cDefine29", length = 120)
    @JsonProperty("cDefine29")
    private String cDefine29;

    @Column(name = "cDefine30", length = 120)
    @JsonProperty("cDefine30")
    private String cDefine30;

    @Column(name = "cDefine31", length = 120)
    @JsonProperty("cDefine31")
    private String cDefine31;

    @Column(name = "cDefine32", length = 120)
    @JsonProperty("cDefine32")
    private String cDefine32;

    @Column(name = "cDefine33", length = 120)
    @JsonProperty("cDefine33")
    private String cDefine33;

    @Column(name = "cDefine34")
    @JsonProperty("cDefine34")
    private Integer cDefine34;

    @Column(name = "cDefine35")
    @JsonProperty("cDefine35")
    private Integer cDefine35;

    @Column(name = "cDefine36")
    @JsonProperty("cDefine36")
    private Date cDefine36;

    @Column(name = "cDefine37")
    @JsonProperty("cDefine37")
    private Date cDefine37;

    @Column(name = "CreateDate")
    @JsonProperty("createDate")
    private Date createDate;

    @Column(name = "CreateTime")
    @JsonProperty("createTime")
    private Date createTime;

    @Column(name = "dBusDate")
    @JsonProperty("dBusDate")
    private Date dBusDate;

    @Column(name = "cBarcodeDefine1", length = 120)
    @JsonProperty("cBarcodeDefine1")
    private String cBarcodeDefine1;

    @Column(name = "cBarcodeDefine2", length = 120)
    @JsonProperty("cBarcodeDefine2")
    private String cBarcodeDefine2;

    @Column(name = "cBarcodeDefine3", length = 120)
    @JsonProperty("cBarcodeDefine3")
    private String cBarcodeDefine3;

    @Column(name = "cBarcodeDefine4", length = 120)
    @JsonProperty("cBarcodeDefine4")
    private String cBarcodeDefine4;

    @Column(name = "cBarcodeDefine5", length = 120)
    @JsonProperty("cBarcodeDefine5")
    private String cBarcodeDefine5;

    @Column(name = "cBarcodeDefine6", length = 120)
    @JsonProperty("cBarcodeDefine6")
    private String cBarcodeDefine6;

    @Column(name = "cBarcodeDefine7", length = 120)
    @JsonProperty("cBarcodeDefine7")
    private String cBarcodeDefine7;

    @Column(name = "cBarcodeDefine8", length = 120)
    @JsonProperty("cBarcodeDefine8")
    private String cBarcodeDefine8;

    @Column(name = "cBarcodeDefine9", length = 120)
    @JsonProperty("cBarcodeDefine9")
    private String cBarcodeDefine9;

    @Column(name = "cBarcodeDefine10", length = 120)
    @JsonProperty("cBarcodeDefine10")
    private String cBarcodeDefine10;

    @Column(name = "cComUnitCode", length = 40)
    @JsonProperty("cComUnitCode")
    private String cComUnitCode;

    @Column(name = "cComAddUnitCode", length = 40)
    @JsonProperty("cComAddUnitCode")
    private String cComAddUnitCode;

    @Column(name = "cSrcCode", length = 30)
    @JsonProperty("cSrcCode")
    private String csrccode;

    @Column(name = "cSrcVouchType", length = 30)
    @JsonProperty("cSrcVouchType")
    private String cSrcVouchType;

    @Column(name = "cSrcSubID")
    @JsonProperty("cSrcSubID")
    private Integer csrcsubid;

    @Column(name = "cBarMainID")
    @JsonProperty("cBarMainID")
    private Integer cBarMainID;

    @Column(name = "cBarMainAutoID")
    @JsonProperty("cBarMainAutoID")
    private Integer cBarMainAutoID;

    @Column(name = "cMaker", length = 50)
    @JsonProperty("cMaker")
    private String cMaker;

    @Column(name = "cGuid", length = 50)
    @JsonProperty("cGuid")
    private String cGuid;

    @Column(name = "cLabelCode", length = 10)
    @JsonProperty("cLabelCode")
    private String cLabelCode;

    @Column(name = "supBarCode", length = 120)
    @JsonProperty("supBarCode")
    private String supBarCode;

    @Column(name = "iPrtCount")
    @JsonProperty("iPrtCount")
    private Integer iPrtCount;

    @Column(name = "iBarCodeState", length = 3)
    @JsonProperty("iBarCodeState")
    private String iBarCodeState;

    @Column(name = "bExpSub")
    @JsonProperty("bExpSub")
    private Integer bExpSub;

    @Column(name = "cNoUseMaker", length = 50)
    @JsonProperty("cNoUseMaker")
    private String cNoUseMaker;

    @Column(name = "dNoUseTime")
    @JsonProperty("dNoUseTime")
    private Date dNoUseTime;

    @Column(name = "bUseLs")
    @JsonProperty("bUseLs")
    private Short bUseLs;

    @Column(name = "cInvBarCode", length = 20)
    @JsonProperty("cInvBarCode")
    private String cInvBarCode;

    @Column(name = "cinvcBarCode", length = 20)
    @JsonProperty("cinvcBarCode")
    private String cinvcBarCode;

    @Column(name = "cWhBarCode", length = 20)
    @JsonProperty("cWhBarCode")
    private String cWhBarCode;

    @Column(name = "cPosBarCode", length = 20)
    @JsonProperty("cPosBarCode")
    private String cPosBarCode;

    @Column(name = "cVenBarCode", length = 20)
    @JsonProperty("cVenBarCode")
    private String cVenBarCode;

    @Column(name = "cSaleUnitCode", length = 40)
    @JsonProperty("cSaleUnitCode")
    private String cSaleUnitCode;

    @Column(name = "iSaleQty", precision = 38, scale = 6)
    @JsonProperty("iSaleQty")
    private java.math.BigDecimal iSaleQty;

    @Column(name = "iSalePrice", precision = 38, scale = 6)
    @JsonProperty("iSalePrice")
    private java.math.BigDecimal iSalePrice;

    @Column(name = "iExpiratDateCalcu")
    @JsonProperty("iExpiratDateCalcu")
    private Short iExpiratDateCalcu;

    @Column(name = "cExpirationdate", length = 10)
    @JsonProperty("cExpirationdate")
    private String cExpirationdate;

    @Column(name = "dExpirationdate")
    @JsonProperty("dExpirationdate")
    private Date dExpirationdate;

    @Column(name = "cBatchProperty1", precision = 38, scale = 6)
    @JsonProperty("cBatchProperty1")
    private java.math.BigDecimal cBatchProperty1;

    @Column(name = "cBatchProperty2", precision = 38, scale = 6)
    @JsonProperty("cBatchProperty2")
    private java.math.BigDecimal cBatchProperty2;

    @Column(name = "cBatchProperty3", precision = 38, scale = 6)
    @JsonProperty("cBatchProperty3")
    private java.math.BigDecimal cBatchProperty3;

    @Column(name = "cBatchProperty4", precision = 38, scale = 6)
    @JsonProperty("cBatchProperty4")
    private java.math.BigDecimal cBatchProperty4;

    @Column(name = "cBatchProperty5", precision = 38, scale = 6)
    @JsonProperty("cBatchProperty5")
    private java.math.BigDecimal cBatchProperty5;

    @Column(name = "cBatchProperty6", length = 120)
    @JsonProperty("cBatchProperty6")
    private String cBatchProperty6;

    @Column(name = "cBatchProperty7", length = 120)
    @JsonProperty("cBatchProperty7")
    private String cBatchProperty7;

    @Column(name = "cBatchProperty8", length = 120)
    @JsonProperty("cBatchProperty8")
    private String cBatchProperty8;

    @Column(name = "cBatchProperty9", length = 120)
    @JsonProperty("cBatchProperty9")
    private String cBatchProperty9;

    @Column(name = "cBatchProperty10")
    @JsonProperty("cBatchProperty10")
    private Date cBatchProperty10;

    @Column(name = "irowno")
    @JsonProperty("irowno")
    private Integer irowno;

    @Column(name = "ufts", insertable = false, updatable = false)
    private byte[] ufts;

    @Column(name = "iRelAutoid")
    @JsonProperty("iRelAutoid")
    private Integer iRelAutoid;

    @Column(name = "cCusCode", length = 50)
    @JsonProperty("cCusCode")
    private String cCusCode;

    @Column(name = "idemandtype")
    @JsonProperty("idemandtype")
    private Byte idemandtype;

    @Column(name = "cdemandcode", length = 30)
    @JsonProperty("cdemandcode")
    private String cdemandcode;

    @Column(name = "idemandseq")
    @JsonProperty("idemandseq")
    private Integer idemandseq;

    @Column(name = "cdemandid", length = 30)
    @JsonProperty("cdemandid")
    private String cdemandid;

    @Column(name = "clastscantype", length = 30)
    @JsonProperty("clastscantype")
    private String clastscantype;

    @Column(name = "vt_id")
    @JsonProperty("vt_id")
    private Integer vt_id;

    @Column(name = "UBarCode", length = 300)
    @JsonProperty("UBarCode")
    private String UBarCode;

    @Column(name = "iPrtPerson", length = 30)
    @JsonProperty("iPrtPerson")
    private String iPrtPerson;
}