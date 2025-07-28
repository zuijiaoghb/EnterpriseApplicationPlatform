package com.enterprise.platform.inventorymanagement.model.sqlserver;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;

@Data
@Entity
@Table(name = "rdrecords10")
public class Rdrecords10 {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "rd_records10_generator")
    @TableGenerator(
        name = "rd_records10_generator",
        table = "sequence_table",
        pkColumnName = "sequence_name",
        valueColumnName = "sequence_value",
        pkColumnValue = "rd_records10_seq",
        allocationSize = 1
    )
    @Column(name = "AutoID")
    private Long autoId;

    @ManyToOne
    @JoinColumn(name = "ID")
    private Rdrecord10 rdrecord10;

    @Column(name = "cInvCode", nullable = false, length = 60)
    private String cInvCode;

    @Column(name = "iNum", precision = 30, scale = 10)
    private BigDecimal iNum;

    @Column(name = "iQuantity", precision = 30, scale = 10)
    private BigDecimal iQuantity;

    @Column(name = "iUnitCost", precision = 30, scale = 10)
    private BigDecimal iUnitCost;

    @Column(name = "iPrice")
    private BigDecimal iPrice;

    @Column(name = "iAPrice")
    private BigDecimal iAPrice;

    @Column(name = "iPUnitCost", precision = 30, scale = 10)
    private BigDecimal iPUnitCost;

    @Column(name = "iPPrice")
    private BigDecimal iPPrice;

    @Column(name = "cBatch", length = 60)
    private String cBatch;

    @Column(name = "cVouchCode")
    private Long cVouchCode;

    @Column(name = "cInVouchCode", length = 30)
    private String cInVouchCode;

    @Column(name = "cinvouchtype", length = 2)
    private String cinvouchtype;

    @Column(name = "iSOutQuantity", precision = 30, scale = 10)
    private BigDecimal iSOutQuantity;

    @Column(name = "iSOutNum", precision = 30, scale = 10)
    private BigDecimal iSOutNum;

    @Column(name = "cFree1", length = 20)
    private String cFree1;

    @Column(name = "cFree2", length = 20)
    private String cFree2;

    @Column(name = "iFlag", nullable = false)
    private Byte iFlag;

    @Column(name = "iFNum", precision = 30, scale = 10)
    private BigDecimal iFNum;

    @Column(name = "iFQuantity", precision = 30, scale = 10)
    private BigDecimal iFQuantity;

    @Column(name = "dVDate")
    private LocalDateTime dVDate;

    @Column(name = "cPosition", length = 20)
    private String cPosition;

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

    @Column(name = "cItem_class", length = 10)
    private String cItemClass;

    @Column(name = "cItemCode", length = 60)
    private String cItemCode;

    @Column(name = "cName", length = 255)
    private String cName;

    @Column(name = "cItemCName", length = 20)
    private String cItemCName;

    @Column(name = "cFree3", length = 20)
    private String cFree3;

    @Column(name = "cFree4", length = 20)
    private String cFree4;

    @Column(name = "cFree5", length = 20)
    private String cFree5;

    @Column(name = "cFree6", length = 20)
    private String cFree6;

    @Column(name = "cFree7", length = 20)
    private String cFree7;

    @Column(name = "cFree8", length = 20)
    private String cFree8;

    @Column(name = "cFree9", length = 20)
    private String cFree9;

    @Column(name = "cFree10", length = 20)
    private String cFree10;

    @Column(name = "cBarCode", length = 200)
    private String cBarCode;

    @Column(name = "iNQuantity", precision = 30, scale = 10)
    private BigDecimal iNQuantity;

    @Column(name = "iNNum", precision = 30, scale = 10)
    private BigDecimal iNNum;

    @Column(name = "cAssUnit", length = 35)
    private String cAssUnit;

    @Column(name = "dMadeDate")
    private LocalDateTime dMadeDate;

    @Column(name = "iMassDate")
    private Integer iMassDate;

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
    private LocalDateTime cDefine36;

    @Column(name = "cDefine37")
    private LocalDateTime cDefine37;

    @Column(name = "iMPoIds")
    private Long iMPoIds;

    @Column(name = "iCheckIds")
    private Long iCheckIds;

    @Column(name = "cBVencode", length = 20)
    private String cBVencode;

    @Column(name = "bGsp")
    private Boolean bGsp;

    @Column(name = "cGspState", length = 20)
    private String cGspState;

    @Column(name = "cCheckCode", length = 30)
    private String cCheckCode;

    @Column(name = "iCheckIdBaks")
    private Long iCheckIdBaks;

    @Column(name = "cRejectCode", length = 30)
    private String cRejectCode;

    @Column(name = "iRejectIds")
    private Long iRejectIds;

    @Column(name = "cCheckPersonCode", length = 20)
    private String cCheckPersonCode;

    @Column(name = "dCheckDate")
    private LocalDateTime dCheckDate;

    @Column(name = "cMassUnit")
    private Short cMassUnit;

    @Column(name = "cMoLotCode", length = 60)
    private String cMoLotCode;

    @Column(name = "bChecked")
    private Boolean bChecked;

    @Column(name = "bRelated")
    private Boolean bRelated;

    @Column(name = "cmworkcentercode", length = 8)
    private String cmworkcentercode;

    @Column(name = "bLPUseFree")
    private Boolean bLPUseFree;

    @Column(name = "iRSRowNO")
    private Integer iRSRowNO;

    @Column(name = "iOriTrackID")
    private Long iOriTrackID;

    @Column(name = "coritracktype", length = 2)
    private String coritracktype;

    @Column(name = "cbaccounter", length = 30)
    private String cbaccounter;

    @Column(name = "dbKeepDate")
    private LocalDateTime dbKeepDate;

    @Column(name = "bCosting")
    private Boolean bCosting;

    @Column(name = "bVMIUsed")
    private Boolean bVMIUsed;

    @Column(name = "iVMISettleQuantity", precision = 30, scale = 10)
    private BigDecimal iVMISettleQuantity;

    @Column(name = "iVMISettleNum", precision = 30, scale = 10)
    private BigDecimal iVMISettleNum;

    @Column(name = "cvmivencode", length = 20)
    private String cvmivencode;

    @Column(name = "iInvSNCount")
    private Integer iInvSNCount;

    @Column(name = "cwhpersoncode", length = 20)
    private String cwhpersoncode;

    @Column(name = "cwhpersonname", length = 50)
    private String cwhpersonname;

    @Column(name = "cserviceoid", length = 38)
    private String cserviceoid;

    @Column(name = "cbserviceoid", length = 38)
    private String cbserviceoid;

    @Column(name = "iinvexchrate", precision = 38, scale = 8)
    private BigDecimal iinvexchrate;

    @Column(name = "corufts", length = 16)
    private String corufts;

    @Column(name = "cmocode", length = 30)
    private String cmocode;

    @Column(name = "imoseq")
    private Integer imoseq;

    @Column(name = "iopseq", length = 10)
    private String iopseq;

    @Column(name = "copdesc", length = 60)
    private String copdesc;

    @Column(name = "strContractGUID")
    private String strContractGUID;

    @Column(name = "iExpiratDateCalcu")
    private Short iExpiratDateCalcu;

    @Column(name = "cExpirationdate", length = 10)
    private String cExpirationdate;

    @Column(name = "dExpirationdate")
    private LocalDateTime dExpirationdate;

    @Column(name = "cciqbookcode", length = 20)
    private String cciqbookcode;

    @Column(name = "iBondedSumQty", precision = 34, scale = 8)
    private BigDecimal iBondedSumQty;

    @Column(name = "productinids")
    private Integer productinids;

    @Column(name = "iorderdid")
    private Integer iorderdid;

    @Column(name = "iordertype")
    private Byte iordertype;

    @Column(name = "iordercode", length = 30)
    private String iordercode;

    @Column(name = "iorderseq")
    private Integer iorderseq;

    @Column(name = "isodid", length = 40)
    private String isodid;

    @Column(name = "isotype")
    private Byte isotype;

    @Column(name = "csocode", length = 40)
    private String csocode;

    @Column(name = "isoseq")
    private Integer isoseq;

    @Column(name = "cBatchProperty1", precision = 38, scale = 8)
    private BigDecimal cBatchProperty1;

    @Column(name = "cBatchProperty2", precision = 38, scale = 8)
    private BigDecimal cBatchProperty2;

    @Column(name = "cBatchProperty3", precision = 38, scale = 8)
    private BigDecimal cBatchProperty3;

    @Column(name = "cBatchProperty4", precision = 38, scale = 8)
    private BigDecimal cBatchProperty4;

    @Column(name = "cBatchProperty5", precision = 38, scale = 8)
    private BigDecimal cBatchProperty5;

    @Column(name = "cBatchProperty6", length = 120)
    private String cBatchProperty6;

    @Column(name = "cBatchProperty7", length = 120)
    private String cBatchProperty7;

    @Column(name = "cBatchProperty8", length = 120)
    private String cBatchProperty8;

    @Column(name = "cBatchProperty9", length = 120)
    private String cBatchProperty9;

    @Column(name = "cBatchProperty10")
    private LocalDateTime cBatchProperty10;

    @Column(name = "cbMemo", length = 255)
    private String cbMemo;

    @Column(name = "irowno")
    private Integer irowno;

    @Column(name = "strowguid")
    private String strowguid;

    @Column(name = "cservicecode", length = 30)
    private String cservicecode;

    @Version
    @Column(name = "rowufts",insertable = false,updatable = false)
    private byte[] rowufts;

    @Column(name = "ipreuseqty", precision = 38, scale = 8)
    private BigDecimal ipreuseqty;

    @Column(name = "ipreuseinum", precision = 38, scale = 8)
    private BigDecimal ipreuseinum;

    @Column(name = "OutCopiedQuantity", precision = 30, scale = 10)
    private BigDecimal OutCopiedQuantity;

    @Column(name = "cbsysbarcode", length = 80)
    private String cbsysbarcode;

    @Column(name = "cplanlotcode", length = 80)
    private String cplanlotcode;

    @Column(name = "bmergecheck")
    private Boolean bmergecheck;

    @Column(name = "imergecheckautoid")
    private Integer imergecheckautoid;

    @Column(name = "iposflag")
    private Short iposflag;

    @Column(name = "iorderdetailid")
    private Long iorderdetailid;

    @Column(name = "body_outid", length = 50)
    private String bodyOutid;
}