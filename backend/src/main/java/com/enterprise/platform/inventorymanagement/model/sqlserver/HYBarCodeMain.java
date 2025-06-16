package com.enterprise.platform.inventorymanagement.model.sqlserver;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "HY_BarCodeMain")
@Data
public class HYBarCodeMain {
    @Id    
    @Column(name = "BarCode", length = 300, nullable = false)
    private String barcode;

    @Column(name = "BarCodeRule", length = 20)
    private String barCodeRule;

    @Column(name = "cInvCode", length = 60)
    private String cInvCode;

    @Column(name = "cVenCode", length = 20)
    private String cVenCode;

    @Column(name = "cWhCode", length = 10)
    private String cWhCode;

    @Column(name = "cPosCode", length = 20)
    private String cPosCode;

    @Column(name = "iInvSaleCost")
    private Double iInvSaleCost;

    @Column(name = "dMdate")
    private Date dMdate;

    @Column(name = "dVdate")
    private Date dVdate;

    @Column(name = "iMassdate")
    private Integer iMassdate;

    @Column(name = "cMassUnit")
    private Short cMassUnit;

    @Column(name = "cChkItemCode", length = 30)
    private String cChkItemCode;

    @Column(name = "cOther", length = 50)
    private String cOther;

    @Column(name = "cHoldItem", length = 50)
    private String cHoldItem;

    @Column(name = "cSHoldItem", length = 50)
    private String cSHoldItem;

    @Column(name = "cSilvItem", length = 50)
    private String cSilvItem;

    @Column(name = "cFree1", length = 40)
    private String cFree1;

    @Column(name = "cFree2", length = 40)
    private String cFree2;

    @Column(name = "cFree3", length = 40)
    private String cFree3;

    @Column(name = "cFree4", length = 40)
    private String cFree4;

    @Column(name = "cFree5", length = 40)
    private String cFree5;

    @Column(name = "cFree6", length = 40)
    private String cFree6;

    @Column(name = "cFree7", length = 40)
    private String cFree7;

    @Column(name = "cFree8", length = 40)
    private String cFree8;

    @Column(name = "cFree9", length = 40)
    private String cFree9;

    @Column(name = "cFree10", length = 40)
    private String cFree10;

    @Column(name = "qty", precision = 38, scale = 6)
    private java.math.BigDecimal qty;

    @Column(name = "iNum", precision = 38, scale = 6)
    private java.math.BigDecimal iNum;

    @Column(name = "pLot", length = 60)
    private String pLot;

    @Column(name = "ichangerate", precision = 38, scale = 6)
    private java.math.BigDecimal ichangerate;

    @Column(name = "cInvSN", length = 30)
    private String cInvSN;

    @Column(name = "cDefine1", length = 20)
    private String cDefine1;

    @Column(name = "cDefine2", length = 20)
    private String cDefine2;

    @Column(name = "cDefine3", length = 20)
    private String cDefine3;

    @Column(name = "cDefine4")
    private Date cDefine4;

    @Column(name = "cDefine5")
    private Integer cDefine5;

    @Column(name = "cDefine6")
    private Date cDefine6;

    @Column(name = "cDefine7")
    private Double cDefine7;

    @Column(name = "cDefine8", length = 4)
    private String cDefine8;

    @Column(name = "cDefine9", length = 8)
    private String cDefine9;

    @Column(name = "cDefine10", length = 60)
    private String cDefine10;

    @Column(name = "cDefine11", length = 120)
    private String cDefine11;

    @Column(name = "cDefine12", length = 120)
    private String cDefine12;

    @Column(name = "cDefine13", length = 120)
    private String cDefine13;

    @Column(name = "cDefine14", length = 120)
    private String cDefine14;

    @Column(name = "cDefine15")
    private Integer cDefine15;

    @Column(name = "cDefine16")
    private Double cDefine16;

    @Column(name = "cDefine22", length = 60)
    private String cDefine22;

    @Column(name = "cDefine23", length = 60)
    private String cDefine23;

    @Column(name = "cDefine24", length = 60)
    private String cDefine24;

    @Column(name = "cDefine25", length = 60)
    private String cDefine25;

    @Column(name = "cDefine26")
    private Double cDefine26;

    @Column(name = "cDefine27")
    private Double cDefine27;

    @Column(name = "cDefine28", length = 120)
    private String cDefine28;

    @Column(name = "cDefine29", length = 120)
    private String cDefine29;

    @Column(name = "cDefine30", length = 120)
    private String cDefine30;

    @Column(name = "cDefine31", length = 120)
    private String cDefine31;

    @Column(name = "cDefine32", length = 120)
    private String cDefine32;

    @Column(name = "cDefine33", length = 120)
    private String cDefine33;

    @Column(name = "cDefine34")
    private Integer cDefine34;

    @Column(name = "cDefine35")
    private Integer cDefine35;

    @Column(name = "cDefine36")
    private Date cDefine36;

    @Column(name = "cDefine37")
    private Date cDefine37;

    @Column(name = "CreateDate")
    private Date createDate;

    @Column(name = "CreateTime")
    private Date createTime;

    @Column(name = "dBusDate")
    private Date dBusDate;

    @Column(name = "cBarcodeDefine1", length = 120)
    private String cBarcodeDefine1;

    @Column(name = "cBarcodeDefine2", length = 120)
    private String cBarcodeDefine2;

    @Column(name = "cBarcodeDefine3", length = 120)
    private String cBarcodeDefine3;

    @Column(name = "cBarcodeDefine4", length = 120)
    private String cBarcodeDefine4;

    @Column(name = "cBarcodeDefine5", length = 120)
    private String cBarcodeDefine5;

    @Column(name = "cBarcodeDefine6", length = 120)
    private String cBarcodeDefine6;

    @Column(name = "cBarcodeDefine7", length = 120)
    private String cBarcodeDefine7;

    @Column(name = "cBarcodeDefine8", length = 120)
    private String cBarcodeDefine8;

    @Column(name = "cBarcodeDefine9", length = 120)
    private String cBarcodeDefine9;

    @Column(name = "cBarcodeDefine10", length = 120)
    private String cBarcodeDefine10;

    @Column(name = "cComUnitCode", length = 40)
    private String cComUnitCode;

    @Column(name = "cComAddUnitCode", length = 40)
    private String cComAddUnitCode;

    @Column(name = "cSrcCode", length = 30)
    private String csrccode;

    @Column(name = "cSrcVouchType", length = 30)
    private String cSrcVouchType;

    @Column(name = "cSrcSubID")
    private Integer cSrcSubID;

    @Column(name = "cBarMainID")
    private Integer cBarMainID;

    @Column(name = "cBarMainAutoID")
    private Integer cBarMainAutoID;

    @Column(name = "cMaker", length = 50)
    private String cMaker;

    @Column(name = "cGuid", length = 50)
    private String cGuid;

    @Column(name = "cLabelCode", length = 10)
    private String cLabelCode;

    @Column(name = "supBarCode", length = 120)
    private String supBarCode;

    @Column(name = "iPrtCount")
    private Integer iPrtCount;

    @Column(name = "iBarCodeState", length = 3)
    private String iBarCodeState;

    @Column(name = "bExpSub")
    private Integer bExpSub;

    @Column(name = "cNoUseMaker", length = 50)
    private String cNoUseMaker;

    @Column(name = "dNoUseTime")
    private Date dNoUseTime;

    @Column(name = "bUseLs")
    private Short bUseLs;

    @Column(name = "cInvBarCode", length = 20)
    private String cInvBarCode;

    @Column(name = "cinvcBarCode", length = 20)
    private String cinvcBarCode;

    @Column(name = "cWhBarCode", length = 20)
    private String cWhBarCode;

    @Column(name = "cPosBarCode", length = 20)
    private String cPosBarCode;

    @Column(name = "cVenBarCode", length = 20)
    private String cVenBarCode;

    @Column(name = "cSaleUnitCode", length = 40)
    private String cSaleUnitCode;

    @Column(name = "iSaleQty", precision = 38, scale = 6)
    private java.math.BigDecimal iSaleQty;

    @Column(name = "iSalePrice", precision = 38, scale = 6)
    private java.math.BigDecimal iSalePrice;

    @Column(name = "iExpiratDateCalcu")
    private Short iExpiratDateCalcu;

    @Column(name = "cExpirationdate", length = 10)
    private String cExpirationdate;

    @Column(name = "dExpirationdate")
    private Date dExpirationdate;

    @Column(name = "cBatchProperty1", precision = 38, scale = 6)
    private java.math.BigDecimal cBatchProperty1;

    @Column(name = "cBatchProperty2", precision = 38, scale = 6)
    private java.math.BigDecimal cBatchProperty2;

    @Column(name = "cBatchProperty3", precision = 38, scale = 6)
    private java.math.BigDecimal cBatchProperty3;

    @Column(name = "cBatchProperty4", precision = 38, scale = 6)
    private java.math.BigDecimal cBatchProperty4;

    @Column(name = "cBatchProperty5", precision = 38, scale = 6)
    private java.math.BigDecimal cBatchProperty5;

    @Column(name = "cBatchProperty6", length = 120)
    private String cBatchProperty6;

    @Column(name = "cBatchProperty7", length = 120)
    private String cBatchProperty7;

    @Column(name = "cBatchProperty8", length = 120)
    private String cBatchProperty8;

    @Column(name = "cBatchProperty9", length = 120)
    private String cBatchProperty9;

    @Column(name = "cBatchProperty10")
    private Date cBatchProperty10;

    @Column(name = "irowno")
    private Integer irowno;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "csrccode", referencedColumnName = "cPOID"),
        @JoinColumn(name = "irowno", referencedColumnName = "irowno")
    })
    private PO_Podetails poPodetails;

    @Column(name = "ufts")
    private byte[] ufts;

    @Column(name = "iRelAutoid")
    private Integer iRelAutoid;

    @Column(name = "cCusCode", length = 50)
    private String cCusCode;

    @Column(name = "idemandtype")
    private Byte idemandtype;

    @Column(name = "cdemandcode", length = 30)
    private String cdemandcode;

    @Column(name = "idemandseq")
    private Integer idemandseq;

    @Column(name = "cdemandid", length = 30)
    private String cdemandid;

    @Column(name = "clastscantype", length = 30)
    private String clastscantype;

    @Column(name = "vt_id")
    private Integer vt_id;

    @Column(name = "UBarCode", length = 300)
    private String UBarCode;

    @Column(name = "iPrtPerson", length = 30)
    private String iPrtPerson;
}