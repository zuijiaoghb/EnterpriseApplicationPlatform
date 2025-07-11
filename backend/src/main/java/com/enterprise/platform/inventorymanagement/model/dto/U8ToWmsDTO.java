package com.enterprise.platform.inventorymanagement.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class U8ToWmsDTO {
    private Long AID;
    private Long ID;
    private String voucherType;
    private String cCode;
    private LocalDate dDate;
    private String cDepCode;
    private String cDepName;
    private String cVenCode;
    private String cVenName;
    private String cWhCode;
    private String cWhName;
    private String cPersonCode;
    private String cPersonName;
    private String cDeliverCode;
    private String cMemo;
    private String cMaker;
    private Long autoID;
    private Integer iRowNo;
    private String cInvCode;
    private String cInvName;
    private String cInvStd;
    private String wb;
    private String cBatch;
    private String cSnCode;
    private String cUnitCode;
    private String cUnitName;
    private BigDecimal iQuantity;
    private BigDecimal iOriTaxCost;
    private BigDecimal iOriTaxMoney;
    private String cbMemo;
    private LocalDateTime u8CreateDatetime;
    private String u8Status;
    private String wmsReadError;
    private String cInvCCode;
}