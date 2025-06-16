package com.enterprise.platform.inventorymanagement.model.sqlserver;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "PO_Pomain")
public class PO_Pomain {
    @Id
    @Column(name = "POID")
    private Integer poid;

    @Column(name = "cPOID")
    private String cPOID;

    @Column(name = "dPODate", nullable = false)
    private LocalDateTime dPODate;

    @Column(name = "cVenCode")
    private String cVenCode;

    @Column(name = "cDepCode")
    private String cDepCode;

    @Column(name = "cPersonCode")
    private String cPersonCode;

    @Column(name = "cPTCode")
    private String cPTCode;

    @Column(name = "cArrivalPlace")
    private String cArrivalPlace;

    @Column(name = "cSCCode")
    private String cSCCode;

    @Column(name = "cexch_name")
    private String cexchName;

    @Column(name = "nflat")
    private Float nflat;

    @Column(name = "iTaxRate")
    private Float iTaxRate;

    @Column(name = "cPayCode")
    private String cPayCode;

    @Column(name = "iCost")
    private BigDecimal iCost;

    @Column(name = "iBargain")
    private BigDecimal iBargain;

    @Column(name = "cMemo")
    private String cMemo;

    @Column(name = "cState")
    private Byte cState;

    @Column(name = "cPeriod")
    private String cPeriod;

    @Column(name = "cMaker")
    private String cMaker;

    @Column(name = "cVerifier")
    private String cVerifier;

    @Column(name = "cCloser")
    private String cCloser;

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
    private Float cDefine7;

    @Column(name = "cDefine8")
    private String cDefine8;

    @Column(name = "cDefine9")
    private String cDefine9;

    @Column(name = "cDefine10")
    private String cDefine10;

    @Column(name = "iVTid", nullable = false)
    private Integer iVTid;

    @Column(name = "ufts")
    private byte[] ufts;

    @Column(name = "cChanger")
    private String cChanger;

    @Column(name = "cBusType")
    private String cBusType;

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
    private Float cDefine16;

    @Column(name = "cLocker")
    private String cLocker;

    @Column(name = "iDiscountTaxType")
    private Byte iDiscountTaxType;

    @Column(name = "iverifystateex")
    private Integer iverifystateex;

    @Column(name = "ireturncount")
    private Integer ireturncount;

    @Column(name = "IsWfControlled")
    private Boolean isWfControlled;

    @Column(name = "cmaketime")
    private LocalDateTime cmaketime;

    @Column(name = "cModifyTime")
    private LocalDateTime cModifyTime;

    @Column(name = "cAuditTime")
    private LocalDateTime cAuditTime;

    @Column(name = "cAuditDate")
    private LocalDateTime cAuditDate;

    @Column(name = "cModifyDate")
    private LocalDateTime cModifyDate;

    @Column(name = "cReviser")
    private String cReviser;

    @Column(name = "cVenPUOMProtocol")
    private String cVenPUOMProtocol;

    @Column(name = "cChangVerifier")
    private String cChangVerifier;

    @Column(name = "cChangAuditTime")
    private LocalDateTime cChangAuditTime;

    @Column(name = "cChangAuditDate")
    private LocalDateTime cChangAuditDate;

    @Column(name = "iBG_OverFlag")
    private Short iBGOverFlag;

    @Column(name = "cBG_Auditor")
    private String cBGAuditor;

    @Column(name = "cBG_AuditTime")
    private String cBGAuditTime;

    @Column(name = "ControlResult")
    private Short controlResult;

    @Column(name = "iflowid")
    private Integer iflowid;

    @Column(name = "iPrintCount")
    private Integer iPrintCount;

    @Column(name = "dCloseDate")
    private LocalDateTime dCloseDate;

    @Column(name = "dCloseTime")
    private LocalDateTime dCloseTime;

    @Column(name = "ccleanver")
    private String ccleanver;

    @Column(name = "cContactCode")
    private String cContactCode;

    @Column(name = "cVenPerson")
    private String cVenPerson;

    @Column(name = "cVenBank")
    private String cVenBank;

    @Column(name = "cVenAccount")
    private String cVenAccount;

    @Column(name = "cappcode")
    private String cappcode;

    @Column(name = "csysbarcode")
    private String csysbarcode;

    @Column(name = "cCurrentAuditor")
    private String cCurrentAuditor;

    @OneToMany(mappedBy = "poPomain", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PO_Podetails> poPodetailsList;

    // Getters and Setters
    public Integer getPoid() { return poid; }
    public void setPoid(Integer poid) { this.poid = poid; }
    public String getcPOID() { return cPOID; }
    public void setcPOID(String cPOID) { this.cPOID = cPOID; }
    public LocalDateTime getdPODate() { return dPODate; }
    public void setdPODate(LocalDateTime dPODate) { this.dPODate = dPODate; }
    public String getcVenCode() { return cVenCode; }
    public void setcVenCode(String cVenCode) { this.cVenCode = cVenCode; }
    public String getcDepCode() { return cDepCode; }
    public void setcDepCode(String cDepCode) { this.cDepCode = cDepCode; }
    public String getcPersonCode() { return cPersonCode; }
    public void setcPersonCode(String cPersonCode) { this.cPersonCode = cPersonCode; }
    public String getcPTCode() { return cPTCode; }
    public void setcPTCode(String cPTCode) { this.cPTCode = cPTCode; }
    public String getcArrivalPlace() { return cArrivalPlace; }
    public void setcArrivalPlace(String cArrivalPlace) { this.cArrivalPlace = cArrivalPlace; }
    public String getcSCCode() { return cSCCode; }
    public void setcSCCode(String cSCCode) { this.cSCCode = cSCCode; }
    public String getCexchName() { return cexchName; }
    public void setCexchName(String cexchName) { this.cexchName = cexchName; }
    public Float getNflat() { return nflat; }
    public void setNflat(Float nflat) { this.nflat = nflat; }
    public Float getiTaxRate() { return iTaxRate; }
    public void setiTaxRate(Float iTaxRate) { this.iTaxRate = iTaxRate; }
    public String getcPayCode() { return cPayCode; }
    public void setcPayCode(String cPayCode) { this.cPayCode = cPayCode; }
    public BigDecimal getiCost() { return iCost; }
    public void setiCost(BigDecimal iCost) { this.iCost = iCost; }
    public BigDecimal getiBargain() { return iBargain; }
    public void setiBargain(BigDecimal iBargain) { this.iBargain = iBargain; }
    public String getcMemo() { return cMemo; }
    public void setcMemo(String cMemo) { this.cMemo = cMemo; }
    public Byte getcState() { return cState; }
    public void setcState(Byte cState) { this.cState = cState; }
    public String getcPeriod() { return cPeriod; }
    public void setcPeriod(String cPeriod) { this.cPeriod = cPeriod; }
    public String getcMaker() { return cMaker; }
    public void setcMaker(String cMaker) { this.cMaker = cMaker; }
    public String getcVerifier() { return cVerifier; }
    public void setcVerifier(String cVerifier) { this.cVerifier = cVerifier; }
    public String getcCloser() { return cCloser; }
    public void setcCloser(String cCloser) { this.cCloser = cCloser; }
    public String getcDefine1() { return cDefine1; }
    public void setcDefine1(String cDefine1) { this.cDefine1 = cDefine1; }
    public String getcDefine2() { return cDefine2; }
    public void setcDefine2(String cDefine2) { this.cDefine2 = cDefine2; }
    public String getcDefine3() { return cDefine3; }
    public void setcDefine3(String cDefine3) { this.cDefine3 = cDefine3; }
    public LocalDateTime getcDefine4() { return cDefine4; }
    public void setcDefine4(LocalDateTime cDefine4) { this.cDefine4 = cDefine4; }
    public Integer getcDefine5() { return cDefine5; }
    public void setcDefine5(Integer cDefine5) { this.cDefine5 = cDefine5; }
    public LocalDateTime getcDefine6() { return cDefine6; }
    public void setcDefine6(LocalDateTime cDefine6) { this.cDefine6 = cDefine6; }
    public Float getcDefine7() { return cDefine7; }
    public void setcDefine7(Float cDefine7) { this.cDefine7 = cDefine7; }
    public String getcDefine8() { return cDefine8; }
    public void setcDefine8(String cDefine8) { this.cDefine8 = cDefine8; }
    public String getcDefine9() { return cDefine9; }
    public void setcDefine9(String cDefine9) { this.cDefine9 = cDefine9; }
    public String getcDefine10() { return cDefine10; }
    public void setcDefine10(String cDefine10) { this.cDefine10 = cDefine10; }
    public Integer getiVTid() { return iVTid; }
    public void setiVTid(Integer iVTid) { this.iVTid = iVTid; }
    public byte[] getUfts() { return ufts; }
    public void setUfts(byte[] ufts) { this.ufts = ufts; }
    public String getcChanger() { return cChanger; }
    public void setcChanger(String cChanger) { this.cChanger = cChanger; }
    public String getcBusType() { return cBusType; }
    public void setcBusType(String cBusType) { this.cBusType = cBusType; }
    public String getcDefine11() { return cDefine11; }
    public void setcDefine11(String cDefine11) { this.cDefine11 = cDefine11; }
    public String getcDefine12() { return cDefine12; }
    public void setcDefine12(String cDefine12) { this.cDefine12 = cDefine12; }
    public String getcDefine13() { return cDefine13; }
    public void setcDefine13(String cDefine13) { this.cDefine13 = cDefine13; }
    public String getcDefine14() { return cDefine14; }
    public void setcDefine14(String cDefine14) { this.cDefine14 = cDefine14; }
    public Integer getcDefine15() { return cDefine15; }
    public void setcDefine15(Integer cDefine15) { this.cDefine15 = cDefine15; }
    public Float getcDefine16() { return cDefine16; }
    public void setcDefine16(Float cDefine16) { this.cDefine16 = cDefine16; }
    public String getcLocker() { return cLocker; }
    public void setcLocker(String cLocker) { this.cLocker = cLocker; }
    public Byte getiDiscountTaxType() { return iDiscountTaxType; }
    public void setiDiscountTaxType(Byte iDiscountTaxType) { this.iDiscountTaxType = iDiscountTaxType; }
    public Integer getIverifystateex() { return iverifystateex; }
    public void setIverifystateex(Integer iverifystateex) { this.iverifystateex = iverifystateex; }
    public Integer getIreturncount() { return ireturncount; }
    public void setIreturncount(Integer ireturncount) { this.ireturncount = ireturncount; }
    public Boolean getIsWfControlled() { return isWfControlled; }
    public void setIsWfControlled(Boolean isWfControlled) { this.isWfControlled = isWfControlled; }
    public LocalDateTime getCmaketime() { return cmaketime; }
    public void setCmaketime(LocalDateTime cmaketime) { this.cmaketime = cmaketime; }
    public LocalDateTime getcModifyTime() { return cModifyTime; }
    public void setcModifyTime(LocalDateTime cModifyTime) { this.cModifyTime = cModifyTime; }
    public LocalDateTime getcAuditTime() { return cAuditTime; }
    public void setcAuditTime(LocalDateTime cAuditTime) { this.cAuditTime = cAuditTime; }
    public LocalDateTime getcAuditDate() { return cAuditDate; }
    public void setcAuditDate(LocalDateTime cAuditDate) { this.cAuditDate = cAuditDate; }
    public LocalDateTime getcModifyDate() { return cModifyDate; }
    public void setcModifyDate(LocalDateTime cModifyDate) { this.cModifyDate = cModifyDate; }
    public String getcReviser() { return cReviser; }
    public void setcReviser(String cReviser) { this.cReviser = cReviser; }
    public String getcVenPUOMProtocol() { return cVenPUOMProtocol; }
    public void setcVenPUOMProtocol(String cVenPUOMProtocol) { this.cVenPUOMProtocol = cVenPUOMProtocol; }
    public String getcChangVerifier() { return cChangVerifier; }
    public void setcChangVerifier(String cChangVerifier) { this.cChangVerifier = cChangVerifier; }
    public LocalDateTime getcChangAuditTime() { return cChangAuditTime; }
    public void setcChangAuditTime(LocalDateTime cChangAuditTime) { this.cChangAuditTime = cChangAuditTime; }
    public LocalDateTime getcChangAuditDate() { return cChangAuditDate; }
    public void setcChangAuditDate(LocalDateTime cChangAuditDate) { this.cChangAuditDate = cChangAuditDate; }
    public Short getiBGOverFlag() { return iBGOverFlag; }
    public void setiBGOverFlag(Short iBGOverFlag) { this.iBGOverFlag = iBGOverFlag; }
    public String getcBGAuditor() { return cBGAuditor; }
    public void setcBGAuditor(String cBGAuditor) { this.cBGAuditor = cBGAuditor; }
    public String getcBGAuditTime() { return cBGAuditTime; }
    public void setcBGAuditTime(String cBGAuditTime) { this.cBGAuditTime = cBGAuditTime; }
    public Short getControlResult() { return controlResult; }
    public void setControlResult(Short controlResult) { this.controlResult = controlResult; }
    public Integer getIflowid() { return iflowid; }
    public void setIflowid(Integer iflowid) { this.iflowid = iflowid; }
    public Integer getiPrintCount() { return iPrintCount; }
    public void setiPrintCount(Integer iPrintCount) { this.iPrintCount = iPrintCount; }
    public LocalDateTime getdCloseDate() { return dCloseDate; }
    public void setdCloseDate(LocalDateTime dCloseDate) { this.dCloseDate = dCloseDate; }
    public LocalDateTime getdCloseTime() { return dCloseTime; }
    public void setdCloseTime(LocalDateTime dCloseTime) { this.dCloseTime = dCloseTime; }
    public String getCcleanver() { return ccleanver; }
    public void setCcleanver(String ccleanver) { this.ccleanver = ccleanver; }
    public String getcContactCode() { return cContactCode; }
    public void setcContactCode(String cContactCode) { this.cContactCode = cContactCode; }
    public String getcVenPerson() { return cVenPerson; }
    public void setcVenPerson(String cVenPerson) { this.cVenPerson = cVenPerson; }
    public String getcVenBank() { return cVenBank; }
    public void setcVenBank(String cVenBank) { this.cVenBank = cVenBank; }
    public String getcVenAccount() { return cVenAccount; }
    public void setcVenAccount(String cVenAccount) { this.cVenAccount = cVenAccount; }
    public String getCappcode() { return cappcode; }
    public void setCappcode(String cappcode) { this.cappcode = cappcode; }
    public String getCsysbarcode() { return csysbarcode; }
    public void setCsysbarcode(String csysbarcode) { this.csysbarcode = csysbarcode; }
    public String getcCurrentAuditor() { return cCurrentAuditor; }
    public void setcCurrentAuditor(String cCurrentAuditor) { this.cCurrentAuditor = cCurrentAuditor; }
    public List<PO_Podetails> getPoPodetailsList() { return poPodetailsList; }
    public void setPoPodetailsList(List<PO_Podetails> poPodetailsList) { this.poPodetailsList = poPodetailsList; }
}