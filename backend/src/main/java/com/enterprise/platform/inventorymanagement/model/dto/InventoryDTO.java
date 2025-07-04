package com.enterprise.platform.inventorymanagement.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InventoryDTO {
    private String cInvCode;
    private String cInvAddCode;
    private String cInvName;
    private String cInvStd;
    private String cInvCCode;
    private String cVenCode;
    private String cPosition;
    private Boolean bSale;
    private Boolean bPurchase;
    private Double iTaxRate;
    private Double iInvSPrice;
    private Double iInvSCost;
    private String cInvABC;
    private LocalDateTime dSDate;
    private LocalDateTime dEDate;
    private String cInvDefine1;
    private String cInvDefine2;
    private String cBarCode;
    private String cEnglishName;
    private String cProduceNation;
    private Double fRetailPrice;
}