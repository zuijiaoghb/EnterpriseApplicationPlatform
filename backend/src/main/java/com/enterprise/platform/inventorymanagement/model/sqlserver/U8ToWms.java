package com.enterprise.platform.inventorymanagement.model.sqlserver;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "_U8ToWms_")
public class U8ToWms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AID;
    @Column(name = "ID")
    private Long ID;
    @Column(name = "VoucherType")
    private String voucherType;
    @Column(name = "cCode")
    private String cCode;
    @Column(name = "dDate")
    private LocalDate dDate;
    @Column(name = "cDepCode")
    private String cDepCode;
    @Column(name = "cDepName")
    private String cDepName;
    @Column(name = "cVenCode")
    private String cVenCode;
    @Column(name = "cVenName")
    private String cVenName;
    @Column(name = "cWhCode")
    private String cWhCode;
    @Column(name = "cWhName")
    private String cWhName;
    @Column(name = "cPersonCode")
    private String cPersonCode;
    @Column(name = "cPersonName")
    private String cPersonName;
    @Column(name = "cDeliverCode")
    private String cDeliverCode;
    @Column(name = "cMemo")
    private String cMemo;
    @Column(name = "cMaker")
    private String cMaker;
    @Column(name = "AutoID")
    private Long autoID;
    @Column(name = "iRowNo")
    private Integer iRowNo;
    @Column(name = "cInvCode")
    private String cInvCode;
    @Column(name = "cInvName")
    private String cInvName;
    @Column(name = "cInvStd")
    private String cInvStd;
    @Column(name = "Wb")
    private String wb;
    @Column(name = "cBatch")
    private String cBatch;
    @Column(name = "cSnCode")
    private String cSnCode;
    @Column(name = "cUnitCode")
    private String cUnitCode;
    @Column(name = "cUnitName")
    private String cUnitName;
    @Column(name = "iQuantity")
    private BigDecimal iQuantity;
    @Column(name = "iOriTaxCost")
    private BigDecimal iOriTaxCost;
    @Column(name = "iOriTaxMoney")
    private BigDecimal iOriTaxMoney;
    @Column(name = "cbMemo")
    private String cbMemo;
    @Column(name = "U8CreateDatetime")
    private LocalDateTime u8CreateDatetime;
    @Column(name = "U8Status")
    private String u8Status;
    @Column(name = "WMSReadError")
    private String wmsReadError;
    @Column(name = "cInvCCode")
    private String cInvCCode;
}