package com.enterprise.platform.inventorymanagement.model.sqlserver;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rdrecords01")
public class RdRecords01 {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "rd_records01_generator")
    @TableGenerator(
        name = "rd_records01_generator",
        table = "sequence_table",
        pkColumnName = "sequence_name",
        valueColumnName = "sequence_value",
        pkColumnValue = "rd_records01_seq",
        allocationSize = 1
    )
    @Column(name = "AutoID")
    private Long autoId;

    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "cInvCode", nullable = false)
    private String cInvCode;

    @Column(name = "iNum")
    private BigDecimal iNum;

    @Column(name = "iQuantity")
    private BigDecimal iQuantity;

    @Column(name = "iUnitCost")
    private BigDecimal iUnitCost;

    @Column(name = "iPrice")
    private BigDecimal iPrice;

    @Column(name = "iAPrice")
    private BigDecimal iAPrice;

    @Column(name = "iPUnitCost")
    private BigDecimal iPUnitCost;

    @Column(name = "iPPrice")
    private BigDecimal iPPrice;

    @Column(name = "cBatch")
    private String cBatch;

    @Column(name = "cVouchCode")
    private Long cVouchCode;

    @Column(name = "cInVouchCode")
    private String cInVouchCode;

    @Column(name = "cinvouchtype")
    private String cinvouchtype;

    @Column(name = "iSOutQuantity")
    private BigDecimal iSOutQuantity;

    @Column(name = "iSOutNum")
    private BigDecimal iSOutNum;

    @Column(name = "cFree1")
    private String cFree1;

    @Column(name = "cFree2")
    private String cFree2;

    @Column(name = "iFlag", nullable = false)
    private Byte iFlag;

    @Column(name = "dSDate")
    private LocalDateTime dSDate;

    @Column(name = "iTax")
    private BigDecimal iTax;

    @Column(name = "iSQuantity")
    private BigDecimal iSQuantity;

    @Column(name = "iSNum")
    private BigDecimal iSNum;

    @Column(name = "iMoney")
    private BigDecimal iMoney;

    @Column(name = "iFNum")
    private BigDecimal iFNum;

    @Column(name = "iFQuantity")
    private BigDecimal iFQuantity;

    @Column(name = "dVDate")
    private LocalDateTime dVDate;

    @Column(name = "cPosition")
    private String cPosition;

    @Column(name = "cDefine22")
    private String cDefine22;

    @Column(name = "cDefine23")
    private String cDefine23;

    @Column(name = "cDefine24")
    private String cDefine24;

    @Column(name = "cDefine25")
    private String cDefine25;

    @Column(name = "cDefine26")
    private Double cDefine26;

    @Column(name = "cDefine27")
    private Double cDefine27;

    @Column(name = "cItem_class")
    private String cItemClass;

    @Column(name = "cItemCode")
    private String cItemCode;

    @Column(name = "iPOsID")
    private Long iPOsID;

    @Column(name = "fACost")
    private BigDecimal fACost;

    @Column(name = "cName")
    private String cName;

    @Column(name = "cItemCName")
    private String cItemCName;

    @Column(name = "cFree3")
    private String cFree3;

    @Column(name = "cFree4")
    private String cFree4;

    @Column(name = "cFree5")
    private String cFree5;

    @Column(name = "cFree6")
    private String cFree6;

    @Column(name = "cFree7")
    private String cFree7;

    @Column(name = "cFree8")
    private String cFree8;

    @Column(name = "cFree9")
    private String cFree9;

    @Column(name = "cFree10")
    private String cFree10;

    @Column(name = "cBarCode")
    private String cBarCode;

    @Column(name = "iNQuantity")
    private BigDecimal iNQuantity;

    @Column(name = "iNNum")
    private BigDecimal iNNum;

    @Column(name = "cAssUnit")
    private String cAssUnit;

    @Column(name = "dMadeDate")
    private LocalDateTime dMadeDate;

    @Column(name = "iMassDate")
    private Integer iMassDate;

    @Column(name = "cDefine28")
    private String cDefine28;

    @Column(name = "cDefine29")
    private String cDefine29;

    @Column(name = "cDefine30")
    private String cDefine30;

    @Column(name = "cDefine31")
    private String cDefine31;

    @Column(name = "cDefine32")
    private String cDefine32;

    @Column(name = "cDefine33")
    private String cDefine33;

    @Column(name = "cDefine34")
    private Integer cDefine34;

    @Column(name = "cDefine35")
    private Integer cDefine35;

    @Column(name = "cDefine36")
    private LocalDateTime cDefine36;

    @Column(name = "cDefine37")
    private LocalDateTime cDefine37;

    @Column(name = "iCheckIds")
    private Long iCheckIds;

    @Column(name = "cBVencode")
    private String cBVencode;

    @Column(name = "chVencode")
    private String chVencode;

    @Column(name = "bGsp")
    private Boolean bGsp;

    @Column(name = "cGspState")
    private String cGspState;

    @Column(name = "iArrsId")
    private Long iArrsId;

    @Column(name = "cCheckCode")
    private String cCheckCode;

    @Column(name = "iCheckIdBaks")
    private Long iCheckIdBaks;

    @Column(name = "cRejectCode")
    private String cRejectCode;

    @Column(name = "iRejectIds")
    private Long iRejectIds;

    @Column(name = "cCheckPersonCode")
    private String cCheckPersonCode;

    @Column(name = "dCheckDate")
    private LocalDateTime dCheckDate;

    @Column(name = "iOriTaxCost")
    private BigDecimal iOriTaxCost;

    @Column(name = "iOriCost")
    private BigDecimal iOriCost;

    @Column(name = "iOriMoney")
    private BigDecimal iOriMoney;

    @Column(name = "iOriTaxPrice")
    private BigDecimal iOriTaxPrice;

    @Column(name = "ioriSum")
    private BigDecimal ioriSum;

    @Column(name = "iTaxRate")
    private BigDecimal iTaxRate;

    @Column(name = "iTaxPrice")
    private BigDecimal iTaxPrice;

    @Column(name = "iSum")
    private BigDecimal iSum;

    @Column(name = "bTaxCost")
    private Boolean bTaxCost;

    @Column(name = "cPOID")
    private String cPOID;

    @Column(name = "cMassUnit")
    private Short cMassUnit;

    @Column(name = "iMaterialFee")
    private BigDecimal iMaterialFee;

    @Column(name = "iProcessCost")
    private BigDecimal iProcessCost;

    @Column(name = "iProcessFee")
    private BigDecimal iProcessFee;

    @Column(name = "dMSDate")
    private LocalDateTime dMSDate;

    @Column(name = "iSMaterialFee")
    private BigDecimal iSMaterialFee;

    @Column(name = "iSProcessFee")
    private BigDecimal iSProcessFee;

    @Column(name = "iOMoDID")
    private Integer iOMoDID;

    @Column(name = "strContractId")
    private String strContractId;

    @Column(name = "strCode")
    private String strCode;

    @Column(name = "bChecked")
    private Boolean bChecked;

    @Column(name = "bRelated")
    private Boolean bRelated;

    @Column(name = "iOMoMID")
    private Long iOMoMID;

    @Column(name = "iMatSettleState", nullable = false)
    private Integer iMatSettleState;

    @Column(name = "iBillSettleCount", nullable = false)
    private Integer iBillSettleCount;

    @Column(name = "bLPUseFree")
    private Boolean bLPUseFree;

    @Column(name = "iOriTrackID")
    private Long iOriTrackID;

    @Column(name = "coritracktype")
    private String coritracktype;

    @Column(name = "cbaccounter")
    private String cbaccounter;

    @Column(name = "dbKeepDate")
    private LocalDateTime dbKeepDate;

    @Column(name = "bCosting")
    private Boolean bCosting;

    @Column(name = "iSumBillQuantity")
    private BigDecimal iSumBillQuantity;

    @Column(name = "bVMIUsed")
    private Boolean bVMIUsed;

    @Column(name = "iVMISettleQuantity")
    private BigDecimal iVMISettleQuantity;

    @Column(name = "iVMISettleNum")
    private BigDecimal iVMISettleNum;

    @Column(name = "cvmivencode")
    private String cvmivencode;

    @Column(name = "iInvSNCount")
    private Integer iInvSNCount;

    @Column(name = "cwhpersoncode")
    private String cwhpersoncode;

    @Column(name = "cwhpersonname")
    private String cwhpersonname;

    @Column(name = "impcost")
    private BigDecimal impcost;

    @Column(name = "iIMOSID")
    private Integer iIMOSID;

    @Column(name = "iIMBSID")
    private Integer iIMBSID;

    @Column(name = "cbarvcode")
    private String cbarvcode;

    @Column(name = "dbarvdate")
    private String dbarvdate;

    @Column(name = "iinvexchrate")
    private BigDecimal iinvexchrate;

    @Column(name = "corufts")
    private String corufts;

    @Column(name = "comcode")
    private String comcode;

    @Column(name = "strContractGUID")
    private String strContractGUID;

    @Column(name = "iExpiratDateCalcu")
    private Short iExpiratDateCalcu;

    @Column(name = "cExpirationdate")
    private String cExpirationdate;

    @Column(name = "dExpirationdate")
    private LocalDateTime dExpirationdate;

    @Column(name = "cciqbookcode")
    private String cciqbookcode;

    @Column(name = "iBondedSumQty")
    private BigDecimal iBondedSumQty;

    @Column(name = "iordertype")
    private Byte iordertype;

    @Column(name = "iorderdid")
    private Integer iorderdid;

    @Column(name = "iordercode")
    private String iordercode;

    @Column(name = "iorderseq")
    private Integer iorderseq;

    @Column(name = "isodid")
    private String isodid;

    @Column(name = "isotype")
    private Byte isotype;

    @Column(name = "csocode")
    private String csocode;

    @Column(name = "isoseq")
    private Integer isoseq;

    @Column(name = "cBatchProperty1")
    private BigDecimal cBatchProperty1;

    @Column(name = "cBatchProperty2")
    private BigDecimal cBatchProperty2;

    @Column(name = "cBatchProperty3")
    private BigDecimal cBatchProperty3;

    @Column(name = "cBatchProperty4")
    private BigDecimal cBatchProperty4;

    @Column(name = "cBatchProperty5")
    private BigDecimal cBatchProperty5;

    @Column(name = "cBatchProperty6")
    private String cBatchProperty6;

    @Column(name = "cBatchProperty7")
    private String cBatchProperty7;

    @Column(name = "cBatchProperty8")
    private String cBatchProperty8;

    @Column(name = "cBatchProperty9")
    private String cBatchProperty9;

    @Column(name = "cBatchProperty10")
    private LocalDateTime cBatchProperty10;

    @Column(name = "cbMemo")
    private String cbMemo;

    @Column(name = "iFaQty")
    private BigDecimal iFaQty;

    @Column(name = "isTax")
    private BigDecimal isTax;

    @Column(name = "irowno")
    private Integer irowno;

    @Column(name = "strowguid")
    private String strowguid;

    @Column(name = "ipreuseqty")
    private BigDecimal ipreuseqty;

    @Column(name = "ipreuseinum")
    private BigDecimal ipreuseinum;

    @Column(name = "iDebitIDs")
    private Integer iDebitIDs;

    @Column(name = "OutCopiedQuantity")
    private BigDecimal OutCopiedQuantity;

    @Column(name = "iOldPartId")
    private Integer iOldPartId;

    @Column(name = "fOldQuantity")
    private BigDecimal fOldQuantity;

    @Column(name = "cbsysbarcode")
    private String cbsysbarcode;

    @Column(name = "bmergecheck")
    private Boolean bmergecheck;

    @Column(name = "iMergeCheckAutoId")
    private Integer iMergeCheckAutoId;

    @Column(name = "bnoitemused")
    private Byte bnoitemused;

    @Column(name = "cReworkMOCode")
    private String cReworkMOCode;

    @Column(name = "iReworkMODetailsid")
    private Integer iReworkMODetailsid;

    @Column(name = "iProductType")
    private Integer iProductType;

    @Column(name = "cMainInvCode")
    private String cMainInvCode;

    @Column(name = "iMainMoDetailsID")
    private Integer iMainMoDetailsID;

    @Column(name = "iShareMaterialFee")
    private BigDecimal iShareMaterialFee;

    @Column(name = "cplanlotcode")
    private String cplanlotcode;

    @Column(name = "bgift")
    private Short bgift;

    @Column(name = "iposflag")
    private Short iposflag;

    @ManyToOne
    @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private RdRecord01 rdRecord01;

    @ManyToOne
    @JoinColumn(name = "cPOID", referencedColumnName = "cPOID", insertable = false, updatable = false)
    private PO_Pomain poPomain;

    @ManyToOne
    @JoinColumn(name = "iPOsID", referencedColumnName = "ID", insertable = false, updatable = false)
    private PO_Podetails poPodetails;
}