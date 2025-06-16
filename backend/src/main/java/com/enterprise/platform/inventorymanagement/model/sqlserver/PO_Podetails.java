package com.enterprise.platform.inventorymanagement.model.sqlserver;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PO_Podetails")
public class PO_Podetails {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "cPOID")
    private String cPOID;

    @Column(name = "cInvCode", nullable = false)
    private String cInvCode;

    @Column(name = "iQuantity")
    private BigDecimal iQuantity;

    @Column(name = "iNum")
    private BigDecimal iNum;

    @Column(name = "iQuotedPrice")
    private BigDecimal iQuotedPrice;

    @Column(name = "iUnitPrice")
    private BigDecimal iUnitPrice;

    @Column(name = "iMoney")
    private BigDecimal iMoney;

    @Column(name = "iTax")
    private BigDecimal iTax;

    @Column(name = "iSum")
    private BigDecimal iSum;

    @Column(name = "iDisCount")
    private BigDecimal iDisCount;

    @Column(name = "iNatUnitPrice")
    private BigDecimal iNatUnitPrice;

    @Column(name = "iNatMoney")
    private BigDecimal iNatMoney;

    @Column(name = "iNatTax")
    private BigDecimal iNatTax;

    @Column(name = "iNatSum")
    private BigDecimal iNatSum;

    @Column(name = "iNatDisCount")
    private BigDecimal iNatDisCount;

    @Column(name = "dArriveDate")
    private LocalDateTime dArriveDate;

    @Column(name = "iReceivedQTY")
    private BigDecimal iReceivedQTY;

    @Column(name = "iReceivedNum")
    private BigDecimal iReceivedNum;

    @Column(name = "iReceivedMoney")
    private BigDecimal iReceivedMoney;

    @Column(name = "iInvQTY")
    private BigDecimal iInvQTY;

    @Column(name = "iInvNum")
    private BigDecimal iInvNum;

    @Column(name = "iInvMoney")
    private BigDecimal iInvMoney;

    @Column(name = "cFree1")
    private String cFree1;

    @Column(name = "cFree2")
    private String cFree2;

    @Column(name = "iNatInvMoney")
    private BigDecimal iNatInvMoney;

    @Column(name = "iOriTotal")
    private BigDecimal iOriTotal;

    @Column(name = "iTotal")
    private BigDecimal iTotal;

    @Column(name = "iPerTaxRate")
    private BigDecimal iPerTaxRate;

    @Column(name = "cDefine22")
    private String cDefine22;

    @Column(name = "cDefine23")
    private String cDefine23;

    @Column(name = "cDefine24")
    private String cDefine24;

    @Column(name = "cDefine25")
    private String cDefine25;

    @Column(name = "cDefine26")
    private Float cDefine26;

    @Column(name = "cDefine27")
    private Float cDefine27;

    @Column(name = "iflag")
    private Byte iflag;

    @Column(name = "cItemCode")
    private String cItemCode;

    @Column(name = "cItem_class")
    private String cItemClass;

    @Column(name = "PPCIds")
    private Integer PPCIds;

    @Column(name = "cItemName")
    private String cItemName;

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

    @Column(name = "bGsp")
    private Byte bGsp;

    @ManyToOne
    @JoinColumn(name = "POID")
    private PO_Pomain poPomain;

    @Column(name = "cUnitID")
    private String cUnitID;

    @Column(name = "iTaxPrice")
    private BigDecimal iTaxPrice;

    @Column(name = "iArrQTY")
    private BigDecimal iArrQTY;

    @Column(name = "iArrNum")
    private BigDecimal iArrNum;

    @Column(name = "iArrMoney")
    private BigDecimal iArrMoney;

    @Column(name = "iNatArrMoney")
    private BigDecimal iNatArrMoney;

    @Column(name = "iAppIds")
    private Integer iAppIds;

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

    @Column(name = "iSOsID")
    private Integer iSOsID;

    @Column(name = "bTaxCost")
    private Boolean bTaxCost;

    @Column(name = "cSource")
    private String cSource;

    @Column(name = "cbCloser")
    private String cbCloser;

    @Column(name = "iPPartId")
    private Integer iPPartId;

    @Column(name = "iPQuantity")
    private BigDecimal iPQuantity;

    @Column(name = "iPTOSeq")
    private Integer iPTOSeq;

    @Column(name = "SoType")
    private Byte SoType;

    @Column(name = "SoDId")
    private String SoDId;

    @Column(name = "ContractRowGUID")
    private UUID ContractRowGUID;

    @Column(name = "cupsocode")
    private String cupsocode;

    @Column(name = "iInvMPCost")
    private BigDecimal iInvMPCost;

    @Column(name = "ContractCode")
    private String ContractCode;

    @Column(name = "ContractRowNo")
    private String ContractRowNo;

    @Column(name = "fPoValidQuantity")
    private BigDecimal fPoValidQuantity;

    @Column(name = "fPoValidNum")
    private BigDecimal fPoValidNum;

    @Column(name = "fPoArrQuantity")
    private BigDecimal fPoArrQuantity;

    @Column(name = "fPoArrNum")
    private BigDecimal fPoArrNum;

    @Column(name = "fPoRetQuantity")
    private BigDecimal fPoRetQuantity;

    @Column(name = "fPoRetNum")
    private BigDecimal fPoRetNum;

    @Column(name = "fPoRefuseQuantity")
    private BigDecimal fPoRefuseQuantity;

    @Column(name = "fPoRefuseNum")
    private BigDecimal fPoRefuseNum;

    @Column(name = "dUfts")
    private byte[] dUfts;

    @Column(name = "iorderdid")
    private Integer iorderdid;

    @Column(name = "iordertype")
    private Byte iordertype;

    @Column(name = "csoordercode")
    private String csoordercode;

    @Column(name = "iorderseq")
    private Integer iorderseq;

    @Column(name = "cbCloseTime")
    private LocalDateTime cbCloseTime;

    @Column(name = "cbCloseDate")
    private LocalDateTime cbCloseDate;

    @Column(name = "cBG_ItemCode")
    private String cBGItemCode;

    @Column(name = "cBG_ItemName")
    private String cBGItemName;

    @Column(name = "cBG_CaliberKey1")
    private String cBGCaliberKey1;

    @Column(name = "cBG_CaliberKeyName1")
    private String cBGCaliberKeyName1;

    @Column(name = "cBG_CaliberKey2")
    private String cBGCaliberKey2;

    @Column(name = "cBG_CaliberKeyName2")
    private String cBGCaliberKeyName2;

    @Column(name = "cBG_CaliberKey3")
    private String cBGCaliberKey3;

    @Column(name = "cBG_CaliberKeyName3")
    private String cBGCaliberKeyName3;

    @Column(name = "cBG_CaliberCode1")
    private String cBGCaliberCode1;

    @Column(name = "cBG_CaliberName1")
    private String cBGCaliberName1;

    @Column(name = "cBG_CaliberCode2")
    private String cBGCaliberCode2;

    @Column(name = "cBG_CaliberName2")
    private String cBGCaliberName2;

    @Column(name = "cBG_CaliberCode3")
    private String cBGCaliberCode3;

    @Column(name = "cBG_CaliberName3")
    private String cBGCaliberName3;

    @Column(name = "iBG_Ctrl")
    private Byte iBGCtrl;

    @Column(name = "cBG_Auditopinion")
    private String cBGAuditopinion;

    @Column(name = "fexquantity")
    private BigDecimal fexquantity;

    @Column(name = "fexnum")
    private BigDecimal fexnum;

    @Column(name = "ivouchrowno")
    private Integer ivouchrowno;

    @Column(name = "cBG_CaliberKey4")
    private String cBGCaliberKey4;

    @Column(name = "cBG_CaliberKeyName4")
    private String cBGCaliberKeyName4;

    @Column(name = "cBG_CaliberKey5")
    private String cBGCaliberKey5;

    @Column(name = "cBG_CaliberKeyName5")
    private String cBGCaliberKeyName5;

    @Column(name = "cBG_CaliberKey6")
    private String cBGCaliberKey6;

    @Column(name = "cBG_CaliberKeyName6")
    private String cBGCaliberKeyName6;

    @Column(name = "cBG_CaliberCode4")
    private String cBGCaliberCode4;

    @Column(name = "cBG_CaliberName4")
    private String cBGCaliberName4;

    @Column(name = "cBG_CaliberCode5")
    private String cBGCaliberCode5;

    @Column(name = "cBG_CaliberName5")
    private String cBGCaliberName5;

    @Column(name = "cBG_CaliberCode6")
    private String cBGCaliberCode6;

    @Column(name = "cBG_CaliberName6")
    private String cBGCaliberName6;

    @Column(name = "csocode")
    private String csocode;

    @Column(name = "irowno")
    private Integer irowno;

    @Column(name = "freceivedqty")
    private BigDecimal freceivedqty;

    @Column(name = "freceivednum")
    private BigDecimal freceivednum;

    @Column(name = "cxjspdids")
    private String cxjspdids;

    @Column(name = "cbMemo")
    private String cbMemo;

    @Column(name = "cbsysbarcode")
    private String cbsysbarcode;

    @Column(name = "planlotnumber")
    private String planlotnumber;

    @Column(name = "bgift")
    private Short bgift;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getcPOID() { return cPOID; }
    public void setcPOID(String cPOID) { this.cPOID = cPOID; }
    public String getcInvCode() { return cInvCode; }
    public void setcInvCode(String cInvCode) { this.cInvCode = cInvCode; }
    public BigDecimal getiQuantity() { return iQuantity; }
    public void setiQuantity(BigDecimal iQuantity) { this.iQuantity = iQuantity; }
    public BigDecimal getiNum() { return iNum; }
    public void setiNum(BigDecimal iNum) { this.iNum = iNum; }
    public BigDecimal getiQuotedPrice() { return iQuotedPrice; }
    public void setiQuotedPrice(BigDecimal iQuotedPrice) { this.iQuotedPrice = iQuotedPrice; }
    public BigDecimal getiUnitPrice() { return iUnitPrice; }
    public void setiUnitPrice(BigDecimal iUnitPrice) { this.iUnitPrice = iUnitPrice; }
    public BigDecimal getiMoney() { return iMoney; }
    public void setiMoney(BigDecimal iMoney) { this.iMoney = iMoney; }
    public BigDecimal getiTax() { return iTax; }
    public void setiTax(BigDecimal iTax) { this.iTax = iTax; }
    public BigDecimal getiSum() { return iSum; }
    public void setiSum(BigDecimal iSum) { this.iSum = iSum; }
    public BigDecimal getiDisCount() { return iDisCount; }
    public void setiDisCount(BigDecimal iDisCount) { this.iDisCount = iDisCount; }
    public BigDecimal getiNatUnitPrice() { return iNatUnitPrice; }
    public void setiNatUnitPrice(BigDecimal iNatUnitPrice) { this.iNatUnitPrice = iNatUnitPrice; }
    public BigDecimal getiNatMoney() { return iNatMoney; }
    public void setiNatMoney(BigDecimal iNatMoney) { this.iNatMoney = iNatMoney; }
    public BigDecimal getiNatTax() { return iNatTax; }
    public void setiNatTax(BigDecimal iNatTax) { this.iNatTax = iNatTax; }
    public BigDecimal getiNatSum() { return iNatSum; }
    public void setiNatSum(BigDecimal iNatSum) { this.iNatSum = iNatSum; }
    public BigDecimal getiNatDisCount() { return iNatDisCount; }
    public void setiNatDisCount(BigDecimal iNatDisCount) { this.iNatDisCount = iNatDisCount; }
    public LocalDateTime getdArriveDate() { return dArriveDate; }
    public void setdArriveDate(LocalDateTime dArriveDate) { this.dArriveDate = dArriveDate; }
    public BigDecimal getiReceivedQTY() { return iReceivedQTY; }
    public void setiReceivedQTY(BigDecimal iReceivedQTY) { this.iReceivedQTY = iReceivedQTY; }
    public BigDecimal getiReceivedNum() { return iReceivedNum; }
    public void setiReceivedNum(BigDecimal iReceivedNum) { this.iReceivedNum = iReceivedNum; }
    public BigDecimal getiReceivedMoney() { return iReceivedMoney; }
    public void setiReceivedMoney(BigDecimal iReceivedMoney) { this.iReceivedMoney = iReceivedMoney; }
    public BigDecimal getiInvQTY() { return iInvQTY; }
    public void setiInvQTY(BigDecimal iInvQTY) { this.iInvQTY = iInvQTY; }
    public BigDecimal getiInvNum() { return iInvNum; }
    public void setiInvNum(BigDecimal iInvNum) { this.iInvNum = iInvNum; }
    public BigDecimal getiInvMoney() { return iInvMoney; }
    public void setiInvMoney(BigDecimal iInvMoney) { this.iInvMoney = iInvMoney; }
    public String getcFree1() { return cFree1; }
    public void setcFree1(String cFree1) { this.cFree1 = cFree1; }
    public String getcFree2() { return cFree2; }
    public void setcFree2(String cFree2) { this.cFree2 = cFree2; }
    public BigDecimal getiNatInvMoney() { return iNatInvMoney; }
    public void setiNatInvMoney(BigDecimal iNatInvMoney) { this.iNatInvMoney = iNatInvMoney; }
    public BigDecimal getiOriTotal() { return iOriTotal; }
    public void setiOriTotal(BigDecimal iOriTotal) { this.iOriTotal = iOriTotal; }
    public BigDecimal getiTotal() { return iTotal; }
    public void setiTotal(BigDecimal iTotal) { this.iTotal = iTotal; }
    public BigDecimal getiPerTaxRate() { return iPerTaxRate; }
    public void setiPerTaxRate(BigDecimal iPerTaxRate) { this.iPerTaxRate = iPerTaxRate; }
    public String getcDefine22() { return cDefine22; }
    public void setcDefine22(String cDefine22) { this.cDefine22 = cDefine22; }
    public String getcDefine23() { return cDefine23; }
    public void setcDefine23(String cDefine23) { this.cDefine23 = cDefine23; }
    public String getcDefine24() { return cDefine24; }
    public void setcDefine24(String cDefine24) { this.cDefine24 = cDefine24; }
    public String getcDefine25() { return cDefine25; }
    public void setcDefine25(String cDefine25) { this.cDefine25 = cDefine25; }
    public Float getcDefine26() { return cDefine26; }
    public void setcDefine26(Float cDefine26) { this.cDefine26 = cDefine26; }
    public Float getcDefine27() { return cDefine27; }
    public void setcDefine27(Float cDefine27) { this.cDefine27 = cDefine27; }
    public Byte getIflag() { return iflag; }
    public void setIflag(Byte iflag) { this.iflag = iflag; }
    public String getcItemCode() { return cItemCode; }
    public void setcItemCode(String cItemCode) { this.cItemCode = cItemCode; }
    public String getcItemClass() { return cItemClass; }
    public void setcItemClass(String cItemClass) { this.cItemClass = cItemClass; }
    public Integer getPPCIds() { return PPCIds; }
    public void setPPCIds(Integer PPCIds) { this.PPCIds = PPCIds; }
    public String getcItemName() { return cItemName; }
    public void setcItemName(String cItemName) { this.cItemName = cItemName; }
    public String getcFree3() { return cFree3; }
    public void setcFree3(String cFree3) { this.cFree3 = cFree3; }
    public String getcFree4() { return cFree4; }
    public void setcFree4(String cFree4) { this.cFree4 = cFree4; }
    public String getcFree5() { return cFree5; }
    public void setcFree5(String cFree5) { this.cFree5 = cFree5; }
    public String getcFree6() { return cFree6; }
    public void setcFree6(String cFree6) { this.cFree6 = cFree6; }
    public String getcFree7() { return cFree7; }
    public void setcFree7(String cFree7) { this.cFree7 = cFree7; }
    public String getcFree8() { return cFree8; }
    public void setcFree8(String cFree8) { this.cFree8 = cFree8; }
    public String getcFree9() { return cFree9; }
    public void setcFree9(String cFree9) { this.cFree9 = cFree9; }
    public String getcFree10() { return cFree10; }
    public void setcFree10(String cFree10) { this.cFree10 = cFree10; }
    public Byte getbGsp() { return bGsp; }
    public void setbGsp(Byte bGsp) { this.bGsp = bGsp; }
    public PO_Pomain getPoPomain() { return poPomain; }
    public void setPoPomain(PO_Pomain poPomain) { this.poPomain = poPomain; }
    public String getcUnitID() { return cUnitID; }
    public void setcUnitID(String cUnitID) { this.cUnitID = cUnitID; }
    public BigDecimal getiTaxPrice() { return iTaxPrice; }
    public void setiTaxPrice(BigDecimal iTaxPrice) { this.iTaxPrice = iTaxPrice; }
    public BigDecimal getiArrQTY() { return iArrQTY; }
    public void setiArrQTY(BigDecimal iArrQTY) { this.iArrQTY = iArrQTY; }
    public BigDecimal getiArrNum() { return iArrNum; }
    public void setiArrNum(BigDecimal iArrNum) { this.iArrNum = iArrNum; }
    public BigDecimal getiArrMoney() { return iArrMoney; }
    public void setiArrMoney(BigDecimal iArrMoney) { this.iArrMoney = iArrMoney; }
    public BigDecimal getiNatArrMoney() { return iNatArrMoney; }
    public void setiNatArrMoney(BigDecimal iNatArrMoney) { this.iNatArrMoney = iNatArrMoney; }
    public Integer getiAppIds() { return iAppIds; }
    public void setiAppIds(Integer iAppIds) { this.iAppIds = iAppIds; }
    public String getcDefine28() { return cDefine28; }
    public void setcDefine28(String cDefine28) { this.cDefine28 = cDefine28; }
    public String getcDefine29() { return cDefine29; }
    public void setcDefine29(String cDefine29) { this.cDefine29 = cDefine29; }
    public String getcDefine30() { return cDefine30; }
    public void setcDefine30(String cDefine30) { this.cDefine30 = cDefine30; }
    public String getcDefine31() { return cDefine31; }
    public void setcDefine31(String cDefine31) { this.cDefine31 = cDefine31; }
    public String getcDefine32() { return cDefine32; }
    public void setcDefine32(String cDefine32) { this.cDefine32 = cDefine32; }
    public String getcDefine33() { return cDefine33; }
    public void setcDefine33(String cDefine33) { this.cDefine33 = cDefine33; }
    public Integer getcDefine34() { return cDefine34; }
    public void setcDefine34(Integer cDefine34) { this.cDefine34 = cDefine34; }
    public Integer getcDefine35() { return cDefine35; }
    public void setcDefine35(Integer cDefine35) { this.cDefine35 = cDefine35; }
    public LocalDateTime getcDefine36() { return cDefine36; }
    public void setcDefine36(LocalDateTime cDefine36) { this.cDefine36 = cDefine36; }
    public LocalDateTime getcDefine37() { return cDefine37; }
    public void setcDefine37(LocalDateTime cDefine37) { this.cDefine37 = cDefine37; }
    public Integer getiSOsID() { return iSOsID; }
    public void setiSOsID(Integer iSOsID) { this.iSOsID = iSOsID; }
    public Boolean getbTaxCost() { return bTaxCost; }
    public void setbTaxCost(Boolean bTaxCost) { this.bTaxCost = bTaxCost; }
    public String getcSource() { return cSource; }
    public void setcSource(String cSource) { this.cSource = cSource; }
    public String getCbCloser() { return cbCloser; }
    public void setCbCloser(String cbCloser) { this.cbCloser = cbCloser; }
    public Integer getiPPartId() { return iPPartId; }
    public void setiPPartId(Integer iPPartId) { this.iPPartId = iPPartId; }
    public BigDecimal getiPQuantity() { return iPQuantity; }
    public void setiPQuantity(BigDecimal iPQuantity) { this.iPQuantity = iPQuantity; }
    public Integer getiPTOSeq() { return iPTOSeq; }
    public void setiPTOSeq(Integer iPTOSeq) { this.iPTOSeq = iPTOSeq; }
    public Byte getSoType() { return SoType; }
    public void setSoType(Byte soType) { SoType = soType; }
    public String getSoDId() { return SoDId; }
    public void setSoDId(String soDId) { SoDId = soDId; }
    public UUID getContractRowGUID() { return ContractRowGUID; }
    public void setContractRowGUID(UUID contractRowGUID) { ContractRowGUID = contractRowGUID; }
    public String getCupsocode() { return cupsocode; }
    public void setCupsocode(String cupsocode) { this.cupsocode = cupsocode; }
    public BigDecimal getiInvMPCost() { return iInvMPCost; }
    public void setiInvMPCost(BigDecimal iInvMPCost) { this.iInvMPCost = iInvMPCost; }
    public String getContractCode() { return ContractCode; }
    public void setContractCode(String contractCode) { ContractCode = contractCode; }
    public String getContractRowNo() { return ContractRowNo; }
    public void setContractRowNo(String contractRowNo) { ContractRowNo = contractRowNo; }
    public BigDecimal getfPoValidQuantity() { return fPoValidQuantity; }
    public void setfPoValidQuantity(BigDecimal fPoValidQuantity) { this.fPoValidQuantity = fPoValidQuantity; }
    public BigDecimal getfPoValidNum() { return fPoValidNum; }
    public void setfPoValidNum(BigDecimal fPoValidNum) { this.fPoValidNum = fPoValidNum; }
    public BigDecimal getfPoArrQuantity() { return fPoArrQuantity; }
    public void setfPoArrQuantity(BigDecimal fPoArrQuantity) { this.fPoArrQuantity = fPoArrQuantity; }
    public BigDecimal getfPoArrNum() { return fPoArrNum; }
    public void setfPoArrNum(BigDecimal fPoArrNum) { this.fPoArrNum = fPoArrNum; }
    public BigDecimal getfPoRetQuantity() { return fPoRetQuantity; }
    public void setfPoRetQuantity(BigDecimal fPoRetQuantity) { this.fPoRetQuantity = fPoRetQuantity; }
    public BigDecimal getfPoRetNum() { return fPoRetNum; }
    public void setfPoRetNum(BigDecimal fPoRetNum) { this.fPoRetNum = fPoRetNum; }
    public BigDecimal getfPoRefuseQuantity() { return fPoRefuseQuantity; }
    public void setfPoRefuseQuantity(BigDecimal fPoRefuseQuantity) { this.fPoRefuseQuantity = fPoRefuseQuantity; }
    public BigDecimal getfPoRefuseNum() { return fPoRefuseNum; }
    public void setfPoRefuseNum(BigDecimal fPoRefuseNum) { this.fPoRefuseNum = fPoRefuseNum; }
    public byte[] getdUfts() { return dUfts; }
    public void setdUfts(byte[] dUfts) { this.dUfts = dUfts; }
    public Integer getIorderdid() { return iorderdid; }
    public void setIorderdid(Integer iorderdid) { this.iorderdid = iorderdid; }
    public Byte getIordertype() { return iordertype; }
    public void setIordertype(Byte iordertype) { this.iordertype = iordertype; }
    public String getCsoordercode() { return csoordercode; }
    public void setCsoordercode(String csoordercode) { this.csoordercode = csoordercode; }
    public Integer getIorderseq() { return iorderseq; }
    public void setIorderseq(Integer iorderseq) { this.iorderseq = iorderseq; }
    public LocalDateTime getCbCloseTime() { return cbCloseTime; }
    public void setCbCloseTime(LocalDateTime cbCloseTime) { this.cbCloseTime = cbCloseTime; }
    public LocalDateTime getCbCloseDate() { return cbCloseDate; }
    public void setCbCloseDate(LocalDateTime cbCloseDate) { this.cbCloseDate = cbCloseDate; }
    public String getcBGItemCode() { return cBGItemCode; }
    public void setcBGItemCode(String cBGItemCode) { this.cBGItemCode = cBGItemCode; }
    public String getcBGItemName() { return cBGItemName; }
    public void setcBGItemName(String cBGItemName) { this.cBGItemName = cBGItemName; }
    public String getcBGCaliberKey1() { return cBGCaliberKey1; }
    public void setcBGCaliberKey1(String cBGCaliberKey1) { this.cBGCaliberKey1 = cBGCaliberKey1; }
    public String getcBGCaliberKeyName1() { return cBGCaliberKeyName1; }
    public void setcBGCaliberKeyName1(String cBGCaliberKeyName1) { this.cBGCaliberKeyName1 = cBGCaliberKeyName1; }
    public String getcBGCaliberKey2() { return cBGCaliberKey2; }
    public void setcBGCaliberKey2(String cBGCaliberKey2) { this.cBGCaliberKey2 = cBGCaliberKey2; }
    public String getcBGCaliberKeyName2() { return cBGCaliberKeyName2; }
    public void setcBGCaliberKeyName2(String cBGCaliberKeyName2) { this.cBGCaliberKeyName2 = cBGCaliberKeyName2; }
    public String getcBGCaliberKey3() { return cBGCaliberKey3; }
    public void setcBGCaliberKey3(String cBGCaliberKey3) { this.cBGCaliberKey3 = cBGCaliberKey3; }
    public String getcBGCaliberKeyName3() { return cBGCaliberKeyName3; }
    public void setcBGCaliberKeyName3(String cBGCaliberKeyName3) { this.cBGCaliberKeyName3 = cBGCaliberKeyName3; }
    public String getcBGCaliberCode1() { return cBGCaliberCode1; }
    public void setcBGCaliberCode1(String cBGCaliberCode1) { this.cBGCaliberCode1 = cBGCaliberCode1; }
    public String getcBGCaliberName1() { return cBGCaliberName1; }
    public void setcBGCaliberName1(String cBGCaliberName1) { this.cBGCaliberName1 = cBGCaliberName1; }
    public String getcBGCaliberCode2() { return cBGCaliberCode2; }
    public void setcBGCaliberCode2(String cBGCaliberCode2) { this.cBGCaliberCode2 = cBGCaliberCode2; }
    public String getcBGCaliberName2() { return cBGCaliberName2; }
    public void setcBGCaliberName2(String cBGCaliberName2) { this.cBGCaliberName2 = cBGCaliberName2; }
    public String getcBGCaliberCode3() { return cBGCaliberCode3; }
    public void setcBGCaliberCode3(String cBGCaliberCode3) { this.cBGCaliberCode3 = cBGCaliberCode3; }
    public String getcBGCaliberName3() { return cBGCaliberName3; }
    public void setcBGCaliberName3(String cBGCaliberName3) { this.cBGCaliberName3 = cBGCaliberName3; }
    public Byte getiBGCtrl() { return iBGCtrl; }
    public void setiBGCtrl(Byte iBGCtrl) { this.iBGCtrl = iBGCtrl; }
    public String getcBGAuditopinion() { return cBGAuditopinion; }
    public void setcBGAuditopinion(String cBGAuditopinion) { this.cBGAuditopinion = cBGAuditopinion; }
    public BigDecimal getFexquantity() { return fexquantity; }
    public void setFexquantity(BigDecimal fexquantity) { this.fexquantity = fexquantity; }
    public BigDecimal getFexnum() { return fexnum; }
    public void setFexnum(BigDecimal fexnum) { this.fexnum = fexnum; }
    public Integer getIvouchrowno() { return ivouchrowno; }
    public void setIvouchrowno(Integer ivouchrowno) { this.ivouchrowno = ivouchrowno; }
    public String getcBGCaliberKey4() { return cBGCaliberKey4; }
    public void setcBGCaliberKey4(String cBGCaliberKey4) { this.cBGCaliberKey4 = cBGCaliberKey4; }
    public String getcBGCaliberKeyName4() { return cBGCaliberKeyName4; }
    public void setcBGCaliberKeyName4(String cBGCaliberKeyName4) { this.cBGCaliberKeyName4 = cBGCaliberKeyName4; }
    public String getcBGCaliberKey5() { return cBGCaliberKey5; }
    public void setcBGCaliberKey5(String cBGCaliberKey5) { this.cBGCaliberKey5 = cBGCaliberKey5; }
    public String getcBGCaliberKeyName5() { return cBGCaliberKeyName5; }
    public void setcBGCaliberKeyName5(String cBGCaliberKeyName5) { this.cBGCaliberKeyName5 = cBGCaliberKeyName5; }
    public String getcBGCaliberKey6() { return cBGCaliberKey6; }
    public void setcBGCaliberKey6(String cBGCaliberKey6) { this.cBGCaliberKey6 = cBGCaliberKey6; }
    public String getcBGCaliberKeyName6() { return cBGCaliberKeyName6; }
    public void setcBGCaliberKeyName6(String cBGCaliberKeyName6) { this.cBGCaliberKeyName6 = cBGCaliberKeyName6; }
    public String getcBGCaliberCode4() { return cBGCaliberCode4; }
    public void setcBGCaliberCode4(String cBGCaliberCode4) { this.cBGCaliberCode4 = cBGCaliberCode4; }
    public String getcBGCaliberName4() { return cBGCaliberName4; }
    public void setcBGCaliberName4(String cBGCaliberName4) { this.cBGCaliberName4 = cBGCaliberName4; }
    public String getcBGCaliberCode5() { return cBGCaliberCode5; }
    public void setcBGCaliberCode5(String cBGCaliberCode5) { this.cBGCaliberCode5 = cBGCaliberCode5; }
    public String getcBGCaliberName5() { return cBGCaliberName5; }
    public void setcBGCaliberName5(String cBGCaliberName5) { this.cBGCaliberName5 = cBGCaliberName5; }
    public String getcBGCaliberCode6() { return cBGCaliberCode6; }
    public void setcBGCaliberCode6(String cBGCaliberCode6) { this.cBGCaliberCode6 = cBGCaliberCode6; }
    public String getcBGCaliberName6() { return cBGCaliberName6; }
    public void setcBGCaliberName6(String cBGCaliberName6) { this.cBGCaliberName6 = cBGCaliberName6; }
    public String getCsocode() { return csocode; }
    public void setCsocode(String csocode) { this.csocode = csocode; }
    public Integer getIrowno() { return irowno; }
    public void setIrowno(Integer irowno) { this.irowno = irowno; }
    public BigDecimal getFreceivedqty() { return freceivedqty; }
    public void setFreceivedqty(BigDecimal freceivedqty) { this.freceivedqty = freceivedqty; }
    public BigDecimal getFreceivednum() { return freceivednum; }
    public void setFreceivednum(BigDecimal freceivednum) { this.freceivednum = freceivednum; }
    public String getCxjspdids() { return cxjspdids; }
    public void setCxjspdids(String cxjspdids) { this.cxjspdids = cxjspdids; }
    public String getCbMemo() { return cbMemo; }
    public void setCbMemo(String cbMemo) { this.cbMemo = cbMemo; }
    public String getCbsysbarcode() { return cbsysbarcode; }
    public void setCbsysbarcode(String cbsysbarcode) { this.cbsysbarcode = cbsysbarcode; }
    public String getPlanlotnumber() { return planlotnumber; }
    public void setPlanlotnumber(String planlotnumber) { this.planlotnumber = planlotnumber; }
    public Short getBgift() { return bgift; }
    public void setBgift(Short bgift) { this.bgift = bgift; }
}