package com.enterprise.platform.inventorymanagement.model.sqlserver;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "RdRecord01")
public class RdRecord01 {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "rd_record01_generator")
    @TableGenerator(
        name = "rd_record01_generator",
        table = "sequence_table",
        pkColumnName = "sequence_name",
        valueColumnName = "sequence_value",
        pkColumnValue = "rd_record01_seq",
        allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "bRdFlag", nullable = false)
    private Byte bRdFlag;

    @Column(name = "cVouchType", nullable = false)
    private String cVouchType;

    @Column(name = "cBusType")
    private String cBusType;

    @Column(name = "cSource", nullable = false)
    private String cSource;

    @Column(name = "cBusCode")
    private String cBusCode;

    @Column(name = "cWhCode", nullable = false)
    private String cWhCode;

    @Column(name = "dDate", nullable = false)
    private LocalDateTime dDate;

    @Column(name = "cCode", nullable = false)
    private String cCode;

    @Column(name = "cRdCode")
    private String cRdCode;

    @Column(name = "cDepCode")
    private String cDepCode;

    @Column(name = "cPersonCode")
    private String cPersonCode;

    @Column(name = "cPTCode")
    private String cPTCode;

    @Column(name = "cSTCode")
    private String cSTCode;

    @Column(name = "cCusCode")
    private String cCusCode;

    @Column(name = "cVenCode")
    private String cVenCode;

    @Column(name = "cOrderCode")
    private String cOrderCode;

    @Column(name = "cARVCode")
    private String cARVCode;

    @Column(name = "cBillCode")
    private Long cBillCode;

    @Column(name = "cDLCode")
    private Long cDLCode;

    @Column(name = "cProBatch")
    private String cProBatch;

    @Column(name = "cHandler")
    private String cHandler;

    @Column(name = "cMemo")
    private String cMemo;

    @Column(name = "bTransFlag", nullable = false)
    private Boolean bTransFlag;

    @Column(name = "cAccounter")
    private String cAccounter;

    @Column(name = "cMaker")
    private String cMaker;

    @Column(name = "cDefine1")
    private String cDefine1;

    @Column(name = "cDefine2")
    private String cDefine2;

    @Column(name = "cDefine3")
    private String cDefine3;

    @Column(name = "cDefine4")
    private LocalDateTime cDefine4;

    @Column(name = "cDefine5")
    private Integer cDefine5;

    @Column(name = "cDefine6")
    private LocalDateTime cDefine6;

    @Column(name = "cDefine7")
    private Double cDefine7;

    @Column(name = "cDefine8")
    private String cDefine8;

    @Column(name = "cDefine9")
    private String cDefine9;

    @Column(name = "cDefine10")
    private String cDefine10;

    @Column(name = "dKeepDate")
    private String dKeepDate;

    @Column(name = "dVeriDate")
    private LocalDateTime dVeriDate;

    @Column(name = "bpufirst")
    private Boolean bpufirst;

    @Column(name = "biafirst")
    private Boolean biafirst;

    @Column(name = "iMQuantity")
    private Double iMQuantity;

    @Column(name = "dARVDate")
    private LocalDateTime dARVDate;

    @Column(name = "cChkCode")
    private String cChkCode;

    @Column(name = "dChkDate")
    private LocalDateTime dChkDate;

    @Column(name = "cChkPerson")
    private String cChkPerson;

    @Column(name = "VT_ID")
    private Integer vtId;

    @Column(name = "bIsSTQc", nullable = false)
    private Boolean bIsSTQc;

    @Column(name = "cDefine11")
    private String cDefine11;

    @Column(name = "cDefine12")
    private String cDefine12;

    @Column(name = "cDefine13")
    private String cDefine13;

    @Column(name = "cDefine14")
    private String cDefine14;

    @Column(name = "cDefine15")
    private Integer cDefine15;

    @Column(name = "cDefine16")
    private Double cDefine16;

    @Column(name = "gspcheck")
    private String gspcheck;

    @Column(name = "ipurorderid")
    private Long ipurorderid;

    @Column(name = "ipurarriveid")
    private Long ipurarriveid;

    @Column(name = "iarriveid")
    private String iarriveid;

    @Column(name = "isalebillid")
    private String isalebillid;

    @Column(name = "iTaxRate")
    private Double iTaxRate;

    @Column(name = "iExchRate")
    private Double iExchRate;

    @Column(name = "cExch_Name")
    private String cExchName;

    @Column(name = "bOMFirst")
    private Boolean bOMFirst;

    @Column(name = "bFromPreYear")
    private Boolean bFromPreYear;

    @Column(name = "bIsLsQuery")
    private Boolean bIsLsQuery;

    @Column(name = "bIsComplement")
    private Short bIsComplement;

    @Column(name = "iDiscountTaxType")
    private Byte iDiscountTaxType;

    @Column(name = "ireturncount")
    private Integer ireturncount;

    @Column(name = "iverifystate")
    private Integer iverifystate;

    @Column(name = "iswfcontrolled")
    private Integer iswfcontrolled;

    @Column(name = "cModifyPerson")
    private String cModifyPerson;

    @Column(name = "dModifyDate")
    private LocalDateTime dModifyDate;

    @Column(name = "dnmaketime")
    private LocalDateTime dnmaketime;

    @Column(name = "dnmodifytime")
    private LocalDateTime dnmodifytime;

    @Column(name = "dnverifytime")
    private LocalDateTime dnverifytime;

    @Column(name = "bredvouch")
    private Integer bredvouch;

    @Column(name = "cVenPUOMProtocol")
    private String cVenPUOMProtocol;

    @Column(name = "dCreditStart")
    private LocalDateTime dCreditStart;

    @Column(name = "iCreditPeriod")
    private Integer iCreditPeriod;

    @Column(name = "dGatheringDate")
    private LocalDateTime dGatheringDate;

    @Column(name = "bCredit")
    private Byte bCredit;

    @Column(name = "iFlowId")
    private Integer iFlowId;

    @Column(name = "cPZID")
    private String cPZID;

    @Column(name = "cSourceLs")
    private String cSourceLs;

    @Column(name = "cSourceCodeLs")
    private String cSourceCodeLs;

    @Column(name = "iPrintCount")
    private Integer iPrintCount;

    @Column(name = "csysbarcode")
    private String csysbarcode;

    @Column(name = "cCurrentAuditor")
    private String cCurrentAuditor;

    @OneToMany(mappedBy = "rdRecord01", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RdRecords01> rdRecords01List;

    @ManyToOne
    @JoinColumn(name = "cCode", referencedColumnName = "cPOID", insertable = false, updatable = false)
    private PO_Pomain poPomain;
}