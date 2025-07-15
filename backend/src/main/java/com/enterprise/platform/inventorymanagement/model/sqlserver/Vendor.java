package com.enterprise.platform.inventorymanagement.model.sqlserver;

import lombok.Data;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Data
@Entity
@Table(name = "Vendor", schema = "dbo")
public class Vendor {
    @Id
    @Column(name = "cVenCode", length = 20, nullable = false)
    private String cVenCode;

    @Column(name = "cVenName", length = 98)
    private String cVenName;

    @Column(name = "cVenAbbName", length = 60, nullable = false)
    private String cVenAbbName;

    @Column(name = "cVCCode", length = 12)
    private String cVCCode;

    @Column(name = "cDCCode", length = 12)
    private String cDCCode;

    @Column(name = "cTrade", length = 50)
    private String cTrade;

    @Column(name = "cVenAddress", length = 255)
    private String cVenAddress;

    @Column(name = "cVenPostCode", length = 20)
    private String cVenPostCode;

    @Column(name = "cVenRegCode", length = 50)
    private String cVenRegCode;

    @Column(name = "cVenBank", length = 100)
    private String cVenBank;

    @Column(name = "cVenAccount", length = 50)
    private String cVenAccount;

    @Column(name = "dVenDevDate")
    private LocalDateTime dVenDevDate;

    @Column(name = "cVenLPerson", length = 100)
    private String cVenLPerson;

    @Column(name = "cVenPhone", length = 100)
    private String cVenPhone;

    @Column(name = "cVenFax", length = 100)
    private String cVenFax;

    @Column(name = "cVenEmail", length = 100)
    private String cVenEmail;

    @Column(name = "cVenPerson", length = 50)
    private String cVenPerson;

    @Column(name = "cVenBP", length = 20)
    private String cVenBP;

    @Column(name = "cVenHand", length = 20)
    private String cVenHand;

    @Column(name = "cVenPPerson", length = 20)
    private String cVenPPerson;

    @Column(name = "iVenDisRate")
    private Double iVenDisRate;

    @Column(name = "iVenCreGrade", length = 6)
    private String iVenCreGrade;

    @Column(name = "iVenCreLine")
    private Double iVenCreLine;

    @Column(name = "iVenCreDate")
    private Integer iVenCreDate;

    @Column(name = "cVenPayCond", length = 20)
    private String cVenPayCond;

    @Column(name = "cVenIAddress", length = 255)
    private String cVenIAddress;

    @Column(name = "cVenIType", length = 10)
    private String cVenIType;

    @Column(name = "cVenHeadCode", length = 20)
    private String cVenHeadCode;

    @Column(name = "cVenWhCode", length = 10)
    private String cVenWhCode;

    @Column(name = "cVenDepart", length = 12)
    private String cVenDepart;

    @Column(name = "iAPMoney")
    private Double iAPMoney;

    @Column(name = "dLastDate")
    private LocalDateTime dLastDate;

    @Column(name = "iLastMoney")
    private Double iLastMoney;

    @Column(name = "dLRDate")
    private LocalDateTime dLRDate;

    @Column(name = "iLRMoney")
    private Double iLRMoney;

    @Column(name = "dEndDate")
    private LocalDateTime dEndDate;

    @Column(name = "iFrequency")
    private Integer iFrequency;

    @Column(name = "bVenTax", nullable = false)
    private Boolean bVenTax;

    @Column(name = "cVenDefine1", length = 20)
    private String cVenDefine1;

    @Column(name = "cVenDefine2", length = 20)
    private String cVenDefine2;

    @Column(name = "cVenDefine3", length = 20)
    private String cVenDefine3;

    @Column(name = "cCreatePerson", length = 20)
    private String cCreatePerson;

    @Column(name = "cModifyPerson", length = 20)
    private String cModifyPerson;

    @Column(name = "dModifyDate")
    private LocalDateTime dModifyDate;

    @Column(name = "cRelCustomer", length = 20)
    private String cRelCustomer;

    @Column(name = "iId")
    private Integer iId;

    @Column(name = "cBarCode", length = 30)
    private String cBarCode;

    @Column(name = "cVenDefine4", length = 60)
    private String cVenDefine4;

    @Column(name = "cVenDefine5", length = 60)
    private String cVenDefine5;

    @Column(name = "cVenDefine6", length = 60)
    private String cVenDefine6;

    @Column(name = "cVenDefine7", length = 120)
    private String cVenDefine7;

    @Column(name = "cVenDefine8", length = 120)
    private String cVenDefine8;

    @Column(name = "cVenDefine9", length = 120)
    private String cVenDefine9;

    @Column(name = "cVenDefine10", length = 120)
    private String cVenDefine10;

    @Column(name = "cVenDefine11")
    private Integer cVenDefine11;

    @Column(name = "cVenDefine12")
    private Integer cVenDefine12;

    @Column(name = "cVenDefine13")
    private Double cVenDefine13;

    @Column(name = "cVenDefine14")
    private Double cVenDefine14;

    @Column(name = "cVenDefine15")
    private LocalDateTime cVenDefine15;

    @Column(name = "cVenDefine16")
    private LocalDateTime cVenDefine16;

    @Version
    @Column(name = "pubufts",insertable = false,updatable = false)
    private Long pubufts;

    @Column(name = "fRegistFund")
    private Double fRegistFund;

    @Column(name = "iEmployeeNum")
    private Integer iEmployeeNum;

    @Column(name = "iGradeABC")
    private Short iGradeABC;

    @Column(name = "cMemo", length = 240)
    private String cMemo;

    @Column(name = "bLicenceDate", nullable = false)
    private Boolean bLicenceDate;

    @Column(name = "dLicenceSDate")
    private LocalDateTime dLicenceSDate;

    @Column(name = "dLicenceEDate")
    private LocalDateTime dLicenceEDate;

    @Column(name = "iLicenceADays")
    private Integer iLicenceADays;

    @Column(name = "bBusinessDate", nullable = false)
    private Boolean bBusinessDate;

    @Column(name = "dBusinessSDate")
    private LocalDateTime dBusinessSDate;

    @Column(name = "dBusinessEDate")
    private LocalDateTime dBusinessEDate;

    @Column(name = "iBusinessADays")
    private Integer iBusinessADays;

    @Column(name = "bProxyDate", nullable = false)
    private Boolean bProxyDate;

    @Column(name = "dProxySDate")
    private LocalDateTime dProxySDate;

    @Column(name = "dProxyEDate")
    private LocalDateTime dProxyEDate;

    @Column(name = "iProxyADays")
    private Integer iProxyADays;

    @Column(name = "bPassGMP", nullable = false)
    private Boolean bPassGMP;

    @Column(name = "bVenCargo", nullable = false)
    private Boolean bVenCargo;

    @Column(name = "bProxyForeign", nullable = false)
    private Boolean bProxyForeign;

    @Column(name = "bVenService", nullable = false)
    private Boolean bVenService;

    @Column(name = "cVenTradeCCode", length = 12)
    private String cVenTradeCCode;

    @Column(name = "cVenBankCode", length = 5)
    private String cVenBankCode;

    @Column(name = "cVenExch_name", length = 50)
    private String cVenExch_name;

    @Column(name = "iVenGSPType", nullable = false)
    private Short iVenGSPType;

    @Column(name = "iVenGSPAuth")
    private Short iVenGSPAuth;

    @Column(name = "cVenGSPAuthNo", length = 40)
    private String cVenGSPAuthNo;

    @Column(name = "cVenBusinessNo", length = 20)
    private String cVenBusinessNo;

    @Column(name = "cVenLicenceNo", length = 20)
    private String cVenLicenceNo;

    @Column(name = "bVenOverseas", nullable = false)
    private Boolean bVenOverseas;

    @Column(name = "bVenAccPeriodMng", nullable = false)
    private Boolean bVenAccPeriodMng;

    @Column(name = "cVenPUOMProtocol", length = 20)
    private String cVenPUOMProtocol;

    @Column(name = "cVenOtherProtocol", length = 20)
    private String cVenOtherProtocol;

    @Column(name = "cVenCountryCode", length = 10)
    private String cVenCountryCode;

    @Column(name = "cVenEnName", length = 100)
    private String cVenEnName;

    @Column(name = "cVenEnAddr1", length = 60)
    private String cVenEnAddr1;

    @Column(name = "cVenEnAddr2", length = 60)
    private String cVenEnAddr2;

    @Column(name = "cVenEnAddr3", length = 60)
    private String cVenEnAddr3;

    @Column(name = "cVenEnAddr4", length = 60)
    private String cVenEnAddr4;

    @Column(name = "cVenPortCode", length = 10)
    private String cVenPortCode;

    @Column(name = "cVenPrimaryVen", length = 20)
    private String cVenPrimaryVen;

    @Column(name = "fVenCommisionRate")
    private Double fVenCommisionRate;

    @Column(name = "fVenInsueRate")
    private Double fVenInsueRate;

    @Column(name = "bVenHomeBranch", nullable = false)
    private Boolean bVenHomeBranch;

    @Column(name = "cVenBranchAddr", length = 200)
    private String cVenBranchAddr;

    @Column(name = "cVenBranchPhone", length = 100)
    private String cVenBranchPhone;

    @Column(name = "cVenBranchPerson", length = 50)
    private String cVenBranchPerson;

    @Column(name = "cVenSSCode", length = 20)
    private String cVenSSCode;

    @Column(name = "cOMWhCode", length = 20)
    private String cOMWhCode;

    @Column(name = "cVenCMProtocol", length = 20)
    private String cVenCMProtocol;

    @Column(name = "cVenIMProtocol", length = 20)
    private String cVenIMProtocol;

    @Column(name = "iVenTaxRate")
    private Double iVenTaxRate;

    @Column(name = "dVenCreateDatetime", nullable = false)
    private LocalDateTime dVenCreateDatetime;

    @Column(name = "cVenMnemCode", length = 98)
    private String cVenMnemCode;

    @Column(name = "cVenContactCode")
    private String cVenContactCode;

    @Column(name = "cvenbankall", length = 50)
    private String cvenbankall;

    @Column(name = "bIsVenAttachFile")
    private Boolean bIsVenAttachFile;

    @Column(name = "cLicenceRange", length = 255)
    private String cLicenceRange;

    @Column(name = "cBusinessRange", length = 255)
    private String cBusinessRange;

    @Column(name = "cVenGSPRange", length = 255)
    private String cVenGSPRange;

    @Column(name = "dVenGSPEDate")
    private LocalDateTime dVenGSPEDate;

    @Column(name = "dVenGSPSDate")
    private LocalDateTime dVenGSPSDate;

    @Column(name = "iVenGSPADays")
    private Integer iVenGSPADays;
}