package com.enterprise.platform.inventorymanagement.model.sqlserver;

import lombok.Data;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "Inventory")
public class Inventory {
    @Id
    @Column(name = "cInvCode", length = 60, nullable = false)
    private String cInvCode;

    @Column(name = "cInvAddCode", length = 255)
    private String cInvAddCode;

    @Column(name = "cInvName", length = 255)
    private String cInvName;

    @Column(name = "cInvStd", length = 255)
    private String cInvStd;

    @Column(name = "cInvCCode", length = 12)
    private String cInvCCode;

    @Column(name = "cVenCode", length = 20)
    private String cVenCode;

    @Column(name = "cReplaceItem", length = 60)
    private String cReplaceItem;

    @Column(name = "cPosition", length = 20)
    private String cPosition;

    @Column(name = "bSale", nullable = false)
    private Boolean bSale;

    @Column(name = "bPurchase", nullable = false)
    private Boolean bPurchase;

    @Column(name = "bSelf", nullable = false)
    private Boolean bSelf;

    @Column(name = "bComsume", nullable = false)
    private Boolean bComsume;

    @Column(name = "bProducing", nullable = false)
    private Boolean bProducing;

    @Column(name = "bService", nullable = false)
    private Boolean bService;

    @Column(name = "bAccessary", nullable = false)
    private Boolean bAccessary;

    @Column(name = "iTaxRate")
    private Double iTaxRate;

    @Column(name = "iInvWeight")
    private Double iInvWeight;

    @Column(name = "iVolume")
    private Double iVolume;

    @Column(name = "iInvRCost")
    private Double iInvRCost;

    @Column(name = "iInvSPrice")
    private Double iInvSPrice;

    @Column(name = "iInvSCost")
    private Double iInvSCost;

    @Column(name = "iInvLSCost")
    private Double iInvLSCost;

    @Column(name = "iInvNCost")
    private Double iInvNCost;

    @Column(name = "iInvAdvance")
    private Double iInvAdvance;

    @Column(name = "iInvBatch")
    private Double iInvBatch;

    @Column(name = "iSafeNum")
    private Double iSafeNum;

    @Column(name = "iTopSum")
    private Double iTopSum;

    @Column(name = "iLowSum")
    private Double iLowSum;

    @Column(name = "iOverStock")
    private Double iOverStock;

    @Column(name = "cInvABC", length = 1)
    private String cInvABC;

    @Column(name = "bInvQuality", nullable = false)
    private Boolean bInvQuality;

    @Column(name = "bInvBatch", nullable = false)
    private Boolean bInvBatch;

    @Column(name = "bInvEntrust", nullable = false)
    private Boolean bInvEntrust;

    @Column(name = "bInvOverStock", nullable = false)
    private Boolean bInvOverStock;

    @Column(name = "dSDate")
    private LocalDateTime dSDate;

    @Column(name = "dEDate")
    private LocalDateTime dEDate;

    @Column(name = "bFree1", nullable = false)
    private Boolean bFree1;

    @Column(name = "bFree2", nullable = false)
    private Boolean bFree2;

    @Column(name = "cInvDefine1", length = 20)
    private String cInvDefine1;

    @Column(name = "cInvDefine2", length = 20)
    private String cInvDefine2;

    @Column(name = "cInvDefine3", length = 20)
    private String cInvDefine3;

    @Column(name = "I_id")
    private Integer IId;

    @Column(name = "bInvType")
    private Boolean bInvType;

    @Column(name = "iInvMPCost")
    private Double iInvMPCost;

    @Column(name = "cQuality", length = 100)
    private String cQuality;

    @Column(name = "iInvSaleCost")
    private Double iInvSaleCost;

    @Column(name = "iInvSCost1")
    private Double iInvSCost1;

    @Column(name = "iInvSCost2")
    private Double iInvSCost2;

    @Column(name = "iInvSCost3")
    private Double iInvSCost3;

    @Column(name = "bFree3", nullable = false)
    private Boolean bFree3;

    @Column(name = "bFree4", nullable = false)
    private Boolean bFree4;

    @Column(name = "bFree5", nullable = false)
    private Boolean bFree5;

    @Column(name = "bFree6", nullable = false)
    private Boolean bFree6;

    @Column(name = "bFree7", nullable = false)
    private Boolean bFree7;

    @Column(name = "bFree8", nullable = false)
    private Boolean bFree8;

    @Column(name = "bFree9", nullable = false)
    private Boolean bFree9;

    @Column(name = "bFree10", nullable = false)
    private Boolean bFree10;

    @Column(name = "cCreatePerson", length = 20)
    private String cCreatePerson;

    @Column(name = "cModifyPerson", length = 20)
    private String cModifyPerson;

    @Column(name = "dModifyDate")
    private LocalDateTime dModifyDate;

    @Column(name = "fSubscribePoint")
    private Double fSubscribePoint;

    @Column(name = "fVagQuantity")
    private Double fVagQuantity;

    @Column(name = "cValueType", length = 20)
    private String cValueType;

    @Column(name = "bFixExch", nullable = false)
    private Boolean bFixExch;

    @Column(name = "fOutExcess")
    private Double fOutExcess;

    @Column(name = "fInExcess")
    private Double fInExcess;

    @Column(name = "iMassDate")
    private Short iMassDate;

    @Column(name = "iWarnDays")
    private Short iWarnDays;

    @Column(name = "fExpensesExch")
    private Double fExpensesExch;

    @Column(name = "bTrack", nullable = false)
    private Boolean bTrack;

    @Column(name = "bSerial", nullable = false)
    private Boolean bSerial;

    @Column(name = "bBarCode", nullable = false)
    private Boolean bBarCode;

    @Column(name = "iId")
    private Integer iId;

    @Column(name = "cBarCode", length = 30)
    private String cBarCode;

    @Column(name = "cInvDefine4", length = 60)
    private String cInvDefine4;

    @Column(name = "cInvDefine5", length = 60)
    private String cInvDefine5;

    @Column(name = "cInvDefine6", length = 60)
    private String cInvDefine6;

    @Column(name = "cInvDefine7", length = 120)
    private String cInvDefine7;

    @Column(name = "cInvDefine8", length = 120)
    private String cInvDefine8;

    @Column(name = "cInvDefine9", length = 120)
    private String cInvDefine9;

    @Column(name = "cInvDefine10", length = 120)
    private String cInvDefine10;

    @Column(name = "cInvDefine11")
    private Integer cInvDefine11;

    @Column(name = "cInvDefine12")
    private Integer cInvDefine12;

    @Column(name = "cInvDefine13")
    private Double cInvDefine13;

    @Column(name = "cInvDefine14")
    private Double cInvDefine14;

    @Column(name = "cInvDefine15")
    private LocalDateTime cInvDefine15;

    @Column(name = "cInvDefine16")
    private LocalDateTime cInvDefine16;

    @Column(name = "iGroupType", nullable = false)
    private Byte iGroupType;

    @Column(name = "cGroupCode", length = 35)
    private String cGroupCode;

    @Column(name = "cComUnitCode", length = 35)
    private String cComUnitCode;

    @Column(name = "cAssComUnitCode", length = 35)
    private String cAssComUnitCode;

    @Column(name = "cSAComUnitCode", length = 35)
    private String cSAComUnitCode;

    @Column(name = "cPUComUnitCode", length = 35)
    private String cPUComUnitCode;

    @Column(name = "cSTComUnitCode", length = 35)
    private String cSTComUnitCode;

    @Column(name = "cCAComUnitCode", length = 35)
    private String cCAComUnitCode;

    @Column(name = "cFrequency", length = 10)
    private String cFrequency;

    @Column(name = "iFrequency")
    private Short iFrequency;

    @Column(name = "iDays")
    private Short iDays;

    @Column(name = "dLastDate")
    private LocalDateTime dLastDate;

    @Column(name = "iWastage")
    private Double iWastage;

    @Column(name = "bSolitude", nullable = false)
    private Boolean bSolitude;

    @Column(name = "cEnterprise", length = 100)
    private String cEnterprise;

    @Column(name = "cAddress", length = 255)
    private String cAddress;

    @Column(name = "cFile", length = 40)
    private String cFile;

    @Column(name = "cLabel", length = 30)
    private String cLabel;

    @Column(name = "cCheckOut", length = 30)
    private String cCheckOut;

    @Column(name = "cLicence", length = 30)
    private String cLicence;

    @Column(name = "bSpecialties", nullable = false)
    private Boolean bSpecialties;

    @Column(name = "cDefWareHouse", length = 10)
    private String cDefWareHouse;

    @Column(name = "iHighPrice")
    private Double iHighPrice;

    @Column(name = "iExpSaleRate")
    private Double iExpSaleRate;

    @Column(name = "cPriceGroup", length = 20)
    private String cPriceGroup;

    @Column(name = "cOfferGrade", length = 20)
    private String cOfferGrade;

    @Column(name = "iOfferRate")
    private Double iOfferRate;

    @Column(name = "cMonth", length = 6)
    private String cMonth;

    @Column(name = "iAdvanceDate")
    private Short iAdvanceDate;

    @Column(name = "cCurrencyName", length = 60)
    private String cCurrencyName;

    @Column(name = "cProduceAddress", length = 255)
    private String cProduceAddress;

    @Column(name = "cProduceNation", length = 60)
    private String cProduceNation;

    @Column(name = "cRegisterNo", length = 60)
    private String cRegisterNo;

    @Column(name = "cEnterNo", length = 60)
    private String cEnterNo;

    @Column(name = "cPackingType", length = 60)
    private String cPackingType;

    @Column(name = "cEnglishName", length = 100)
    private String cEnglishName;

    @Column(name = "bPropertyCheck", nullable = false)
    private Boolean bPropertyCheck;

    @Column(name = "cPreparationType", length = 30)
    private String cPreparationType;

    @Column(name = "cCommodity", length = 60)
    private String cCommodity;

    @Column(name = "iRecipeBatch", nullable = false)
    private Byte iRecipeBatch;

    @Column(name = "cNotPatentName", length = 30)
    private String cNotPatentName;

    @Column(name = "pubufts")
    private byte[] pubufts;

    @Column(name = "bPromotSales", nullable = false)
    private Boolean bPromotSales;

    @Column(name = "iPlanPolicy")
    private Short iPlanPolicy;

    @Column(name = "iROPMethod")
    private Short iROPMethod;

    @Column(name = "iBatchRule")
    private Short iBatchRule;

    @Column(name = "fBatchIncrement")
    private Double fBatchIncrement;

    @Column(name = "iAssureProvideDays")
    private Integer iAssureProvideDays;

    @Column(name = "iTestStyle")
    private Short iTestStyle;

    @Column(name = "iDTMethod")
    private Short iDTMethod;

    @Column(name = "fDTRate")
    private Double fDTRate;

    @Column(name = "fDTNum")
    private Double fDTNum;

    @Column(name = "cDTUnit", length = 35)
    private String cDTUnit;

    @Column(name = "iDTStyle")
    private Short iDTStyle;

    @Column(name = "iQTMethod")
    private Integer iQTMethod;

    @Column(name = "PictureGUID")
    private String pictureGUID;

    @Column(name = "bPlanInv", nullable = false)
    private Boolean bPlanInv;

    @Column(name = "bProxyForeign", nullable = false)
    private Boolean bProxyForeign;

    @Column(name = "bATOModel", nullable = false)
    private Boolean bATOModel;

    @Column(name = "bCheckItem", nullable = false)
    private Boolean bCheckItem;

    @Column(name = "bPTOModel", nullable = false)
    private Boolean bPTOModel;

    @Column(name = "bEquipment", nullable = false)
    private Boolean bEquipment;

    @Column(name = "cProductUnit", length = 35)
    private String cProductUnit;

    @Column(name = "fOrderUpLimit")
    private Double fOrderUpLimit;

    @Column(name = "cMassUnit")
    private Short cMassUnit;

    @Column(name = "fRetailPrice")
    private Double fRetailPrice;

    @Column(name = "cInvDepCode", length = 12)
    private String cInvDepCode;

    @Column(name = "iAlterAdvance")
    private Integer iAlterAdvance;

    @Column(name = "fAlterBaseNum")
    private Double fAlterBaseNum;

    @Column(name = "cPlanMethod", length = 1)
    private String cPlanMethod;

    @Column(name = "bMPS", nullable = false)
    private Boolean bMPS;

    @Column(name = "bROP", nullable = false)
    private Boolean bROP;

    @Column(name = "bRePlan", nullable = false)
    private Boolean bRePlan;

    @Column(name = "cSRPolicy", length = 2)
    private String cSRPolicy;

    @Column(name = "bBillUnite", nullable = false)
    private Boolean bBillUnite;

    @Column(name = "iSupplyDay")
    private Integer iSupplyDay;

    @Column(name = "fSupplyMulti")
    private Double fSupplyMulti;

    @Column(name = "fMinSupply")
    private Double fMinSupply;

    @Column(name = "bCutMantissa", nullable = false)
    private Boolean bCutMantissa;

    @Column(name = "cInvPersonCode", length = 20)
    private String cInvPersonCode;

    @Column(name = "iInvTfId")
    private Integer iInvTfId;

    @Column(name = "cEngineerFigNo", length = 60)
    private String cEngineerFigNo;

    @Column(name = "bInTotalCost", nullable = false)
    private Boolean bInTotalCost;

    @Column(name = "iSupplyType", nullable = false)
    private Short iSupplyType;

    @Column(name = "bConfigFree1", nullable = false)
    private Boolean bConfigFree1;

    @Column(name = "bConfigFree2", nullable = false)
    private Boolean bConfigFree2;

    @Column(name = "bConfigFree3", nullable = false)
    private Boolean bConfigFree3;

    @Column(name = "bConfigFree4", nullable = false)
    private Boolean bConfigFree4;

    @Column(name = "bConfigFree5", nullable = false)
    private Boolean bConfigFree5;

    @Column(name = "bConfigFree6", nullable = false)
    private Boolean bConfigFree6;

    @Column(name = "bConfigFree7", nullable = false)
    private Boolean bConfigFree7;

    @Column(name = "bConfigFree8", nullable = false)
    private Boolean bConfigFree8;

    @Column(name = "bConfigFree9", nullable = false)
    private Boolean bConfigFree9;

    @Column(name = "bConfigFree10", nullable = false)
    private Boolean bConfigFree10;

    @Column(name = "iDTLevel")
    private Short iDTLevel;

    @Column(name = "cDTAQL", length = 20)
    private String cDTAQL;

    @Column(name = "bPeriodDT", nullable = false)
    private Boolean bPeriodDT;

    @Column(name = "cDTPeriod", length = 30)
    private String cDTPeriod;

    @Column(name = "iBigMonth")
    private Integer iBigMonth;

    @Column(name = "iBigDay")
    private Integer iBigDay;

    @Column(name = "iSmallMonth")
    private Integer iSmallMonth;

    @Column(name = "iSmallDay")
    private Integer iSmallDay;

    @Column(name = "bOutInvDT", nullable = false)
    private Boolean bOutInvDT;

    @Column(name = "bBackInvDT", nullable = false)
    private Boolean bBackInvDT;

    @Column(name = "iEndDTStyle")
    private Short iEndDTStyle;

    @Column(name = "bDTWarnInv")
    private Boolean bDTWarnInv;

    @Column(name = "fBackTaxRate")
    private Double fBackTaxRate;

    @Column(name = "cCIQCode", length = 30)
    private String cCIQCode;

    @Column(name = "cWGroupCode", length = 35)
    private String cWGroupCode;

    @Column(name = "cWUnit", length = 35)
    private String cWUnit;

    @Column(name = "fGrossW")
    private Double fGrossW;

    @Column(name = "cVGroupCode", length = 35)
    private String cVGroupCode;

    @Column(name = "cVUnit", length = 35)
    private String cVUnit;

    @Column(name = "fLength")
    private Double fLength;

    @Column(name = "fWidth")
    private Double fWidth;

    @Column(name = "fHeight")
    private Double fHeight;

    @Column(name = "iDTUCounter")
    private Integer iDTUCounter;

    @Column(name = "iDTDCounter")
    private Integer iDTDCounter;

    @Column(name = "iBatchCounter")
    private Integer iBatchCounter;

    @Column(name = "cShopUnit", length = 35)
    private String cShopUnit;

    @Column(name = "cPurPersonCode", length = 20)
    private String cPurPersonCode;

    @Column(name = "bImportMedicine", nullable = false)
    private Boolean bImportMedicine;

    @Column(name = "bFirstBusiMedicine", nullable = false)
    private Boolean bFirstBusiMedicine;

    @Column(name = "bForeExpland", nullable = false)
    private Boolean bForeExpland;

    @Column(name = "cInvPlanCode", length = 20)
    private String cInvPlanCode;

    @Column(name = "fConvertRate", nullable = false)
    private Double fConvertRate;

    @Column(name = "dReplaceDate")
    private LocalDateTime dReplaceDate;

    @Column(name = "bInvModel", nullable = false)
    private Boolean bInvModel;

    @Column(name = "bKCCutMantissa", nullable = false)
    private Boolean bKCCutMantissa;

    @Column(name = "bReceiptByDT")
    private Boolean bReceiptByDT;

    @Column(name = "iImpTaxRate")
    private Double iImpTaxRate;

    @Column(name = "iExpTaxRate")
    private Double iExpTaxRate;

    @Column(name = "bExpSale")
    private Boolean bExpSale;

    @Column(name = "iDrawBatch")
    private Double iDrawBatch;

    @Column(name = "bCheckBSATP", nullable = false)
    private Boolean bCheckBSATP;

    @Column(name = "cInvProjectCode", length = 16)
    private String cInvProjectCode;

    @Column(name = "iTestRule")
    private Short iTestRule;

    @Column(name = "cRuleCode", length = 20)
    private String cRuleCode;

    @Column(name = "bCheckFree1", nullable = false)
    private Boolean bCheckFree1;

    @Column(name = "bCheckFree2", nullable = false)
    private Boolean bCheckFree2;

    @Column(name = "bCheckFree3", nullable = false)
    private Boolean bCheckFree3;

    @Column(name = "bCheckFree4", nullable = false)
    private Boolean bCheckFree4;

    @Column(name = "bCheckFree5", nullable = false)
    private Boolean bCheckFree5;

    @Column(name = "bCheckFree6", nullable = false)
    private Boolean bCheckFree6;

    @Column(name = "bCheckFree7", nullable = false)
    private Boolean bCheckFree7;

    @Column(name = "bCheckFree8", nullable = false)
    private Boolean bCheckFree8;

    @Column(name = "bCheckFree9", nullable = false)
    private Boolean bCheckFree9;

    @Column(name = "bCheckFree10", nullable = false)
    private Boolean bCheckFree10;

    @Column(name = "bBomMain", nullable = false)
    private Boolean bBomMain;

    @Column(name = "bBomSub", nullable = false)
    private Boolean bBomSub;

    @Column(name = "bProductBill", nullable = false)
    private Boolean bProductBill;

    @Column(name = "iCheckATP", nullable = false)
    private Short iCheckATP;

    @Column(name = "iInvATPId")
    private Integer iInvATPId;

    @Column(name = "iPlanTfDay")
    private Integer iPlanTfDay;

    @Column(name = "iOverlapDay")
    private Integer iOverlapDay;

    @Column(name = "bPiece", nullable = false)
    private Boolean bPiece;

    @Column(name = "bSrvItem", nullable = false)
    private Boolean bSrvItem;

    @Column(name = "bSrvFittings", nullable = false)
    private Boolean bSrvFittings;

    @Column(name = "fMaxSupply")
    private Double fMaxSupply;

    @Column(name = "fMinSplit")
    private Double fMinSplit;

    @Column(name = "bSpecialOrder", nullable = false)
    private Boolean bSpecialOrder;

    @Column(name = "bTrackSaleBill", nullable = false)
    private Boolean bTrackSaleBill;

    @Column(name = "cInvMnemCode", length = 40)
    private String cInvMnemCode;

    @Column(name = "iPlanDefault")
    private Short iPlanDefault;

    @Column(name = "iPFBatchQty")
    private Double iPFBatchQty;

    @Column(name = "iAllocatePrintDgt")
    private Integer iAllocatePrintDgt;

    @Column(name = "bCheckBatch", nullable = false)
    private Boolean bCheckBatch;

    @Column(name = "bMngOldpart", nullable = false)
    private Boolean bMngOldpart;

    @Column(name = "iOldpartMngRule")
    private Short iOldpartMngRule;
}