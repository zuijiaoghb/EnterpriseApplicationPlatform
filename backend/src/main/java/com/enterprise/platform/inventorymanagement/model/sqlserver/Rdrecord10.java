package com.enterprise.platform.inventorymanagement.model.sqlserver;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;

@Data
@Entity
@Table(name = "rdrecord10")
public class Rdrecord10 {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "rd_record10_generator")
    @TableGenerator(
        name = "rd_record10_generator",
        table = "sequence_table",
        pkColumnName = "sequence_name",
        valueColumnName = "sequence_value",
        pkColumnValue = "rd_record10_seq",
        allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "bRdFlag", nullable = false)
    private Byte bRdFlag;

    @Column(name = "cVouchType", nullable = false, length = 2)
    private String cVouchType;

    @Column(name = "cBusType", length = 12)
    private String cBusType;

    @Column(name = "cSource", nullable = false, length = 50)
    private String cSource;

    @Column(name = "cBusCode", length = 30)
    private String cBusCode;

    @Column(name = "cWhCode", nullable = false, length = 10)
    private String cWhCode;

    @Column(name = "dDate", nullable = false)
    private LocalDateTime dDate;

    @Column(name = "cCode", nullable = false, length = 30)
    private String cCode;

    @Column(name = "cRdCode", length = 5)
    private String cRdCode;

    @Column(name = "cDepCode", length = 12)
    private String cDepCode;

    @Column(name = "cPersonCode", length = 20)
    private String cPersonCode;

    @Column(name = "cPTCode", length = 2)
    private String cPTCode;

    @Column(name = "cSTCode", length = 2)
    private String cSTCode;

    @Column(name = "cCusCode", length = 20)
    private String cCusCode;

    @Column(name = "cVenCode", length = 20)
    private String cVenCode;

    @Column(name = "cOrderCode", length = 30)
    private String cOrderCode;

    @Column(name = "cARVCode", length = 30)
    private String cARVCode;

    @Column(name = "cBillCode")
    private Long cBillCode;

    @Column(name = "cDLCode")
    private Long cDLCode;

    @Column(name = "cProBatch", length = 60)
    private String cProBatch;

    @Column(name = "cHandler", length = 20)
    private String cHandler;

    @Column(name = "cMemo", length = 255)
    private String cMemo;

    @Column(name = "bTransFlag", nullable = false)
    private Boolean bTransFlag;

    @Column(name = "cAccounter", length = 20)
    private String cAccounter;

    @Column(name = "cMaker", length = 20)
    private String cMaker;

    @Column(name = "cDefine1", length = 20)
    private String cDefine1;

    @Column(name = "cDefine2", length = 20)
    private String cDefine2;

    @Column(name = "cDefine3", length = 20)
    private String cDefine3;

    @Column(name = "cDefine4")
    private LocalDateTime cDefine4;

    @Column(name = "cDefine5")
    private Integer cDefine5;

    @Column(name = "cDefine6")
    private LocalDateTime cDefine6;

    @Column(name = "cDefine7")
    private Double cDefine7;

    @Column(name = "cDefine8", length = 4)
    private String cDefine8;

    @Column(name = "cDefine9", length = 8)
    private String cDefine9;

    @Column(name = "cDefine10", length = 60)
    private String cDefine10;

    @Column(name = "dKeepDate", length = 5)
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

    @Column(name = "cChkCode", length = 30)
    private String cChkCode;

    @Column(name = "dChkDate")
    private LocalDateTime dChkDate;

    @Column(name = "cChkPerson", length = 40)
    private String cChkPerson;

    @Column(name = "VT_ID")
    private Integer vtId;

    @Column(name = "bIsSTQc", nullable = false)
    private Boolean bIsSTQc;

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

    @Column(name = "cMPoCode", length = 30)
    private String cMPoCode;

    @Column(name = "gspcheck", length = 10)
    private String gspcheck;

    @Column(name = "ipurorderid")
    private Long ipurorderid;

    @Column(name = "iproorderid")
    private Long iproorderid;

    @Version
    @Column(name = "ufts",insertable = false,updatable = false)
    private Long ufts;

    @Column(name = "iExchRate")
    private Double iExchRate;

    @Column(name = "cExch_Name", length = 8)
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

    @Column(name = "cModifyPerson", length = 50)
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

    @Column(name = "iFlowId")
    private Integer iFlowId;

    @Column(name = "cPZID", length = 30)
    private String cPZID;

    @Column(name = "cSourceLs", length = 4)
    private String cSourceLs;

    @Column(name = "cSourceCodeLs", length = 20)
    private String cSourceCodeLs;

    @Column(name = "iPrintCount")
    private Integer iPrintCount;

    @Column(name = "csysbarcode", length = 60)
    private String csysbarcode;

    @Column(name = "cCurrentAuditor", length = 200)
    private String cCurrentAuditor;

    @Column(name = "outid")
    private String outid;

    @OneToMany(mappedBy = "rdrecord10", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rdrecords10> rdrecords10List;
}