package com.enterprise.platform.inventorymanagement.model.sqlserver;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "CurrentStock")
public class CurrentStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AutoID")
    private Integer autoId;

    @Column(name = "cWhCode", nullable = false, length = 10)
    private String cWhCode;

    @Column(name = "cInvCode", length = 60)
    private String cInvCode;

    @Column(name = "ItemId", nullable = false)
    private Integer itemId;

    @Column(name = "cBatch", nullable = false, length = 60)
    private String cBatch = "";

    @Column(name = "cVMIVenCode", nullable = false, length = 20)
    private String cVMIVenCode = "";

    @Column(name = "iSoType", nullable = false)
    private Integer iSoType = 0;

    @Column(name = "iSodid", length = 40)
    private String iSodid;

    @Column(name = "iQuantity", precision = 38, scale = 6)
    private BigDecimal iQuantity;

    @Column(name = "iNum", precision = 38, scale = 6)
    private BigDecimal iNum;

    @Column(name = "cFree1", length = 20)
    private String cFree1 = "";

    @Column(name = "cFree2", length = 20)
    private String cFree2 = "";

    @Column(name = "fOutQuantity", precision = 38, scale = 6)
    private BigDecimal fOutQuantity;

    @Column(name = "fOutNum", precision = 38, scale = 6)
    private BigDecimal fOutNum;

    @Column(name = "fInQuantity", precision = 38, scale = 6)
    private BigDecimal fInQuantity;

    @Column(name = "fInNum", precision = 38, scale = 6)
    private BigDecimal fInNum;

    @Column(name = "cFree3", length = 20)
    private String cFree3 = "";

    @Column(name = "cFree4", length = 20)
    private String cFree4 = "";

    @Column(name = "cFree5", length = 20)
    private String cFree5 = "";

    @Column(name = "cFree6", length = 20)
    private String cFree6 = "";

    @Column(name = "cFree7", length = 20)
    private String cFree7 = "";

    @Column(name = "cFree8", length = 20)
    private String cFree8 = "";

    @Column(name = "cFree9", length = 20)
    private String cFree9 = "";

    @Column(name = "cFree10", length = 20)
    private String cFree10 = "";

    @Column(name = "dVDate")
    private LocalDateTime dVDate;

    @Column(name = "bStopFlag", nullable = false)
    private Boolean bStopFlag = false;

    @Column(name = "fTransInQuantity", precision = 38, scale = 6)
    private BigDecimal fTransInQuantity;

    @Column(name = "dMdate")
    private LocalDateTime dMdate;

    @Column(name = "fTransInNum", precision = 38, scale = 6)
    private BigDecimal fTransInNum;

    @Column(name = "fTransOutQuantity", precision = 38, scale = 6)
    private BigDecimal fTransOutQuantity;

    @Column(name = "fTransOutNum", precision = 38, scale = 6)
    private BigDecimal fTransOutNum;

    @Column(name = "fPlanQuantity", precision = 38, scale = 6)
    private BigDecimal fPlanQuantity;

    @Column(name = "fPlanNum", precision = 38, scale = 6)
    private BigDecimal fPlanNum;

    @Column(name = "fDisableQuantity", precision = 38, scale = 6)
    private BigDecimal fDisableQuantity;

    @Column(name = "fDisableNum", precision = 38, scale = 6)
    private BigDecimal fDisableNum;

    @Column(name = "fAvaQuantity", precision = 38, scale = 6)
    private BigDecimal fAvaQuantity;

    @Column(name = "fAvaNum", precision = 38, scale = 6)
    private BigDecimal fAvaNum;

    @Column(name = "ufts",insertable = false,updatable = false)
    private byte[] ufts;

    @Column(name = "iMassDate")
    private Integer iMassDate;

    @Column(name = "BGSPSTOP", nullable = false)
    private Boolean BGSPSTOP = false;

    @Column(name = "cMassUnit")
    private Short cMassUnit;

    @Column(name = "fStopQuantity", precision = 38, scale = 6)
    private BigDecimal fStopQuantity;

    @Column(name = "fStopNum", precision = 38, scale = 6)
    private BigDecimal fStopNum;

    @Column(name = "dLastCheckDate")
    private LocalDateTime dLastCheckDate;

    @Column(name = "cCheckState", nullable = false, length = 4)
    private String cCheckState = "";

    @Column(name = "dLastYearCheckDate")
    private LocalDateTime dLastYearCheckDate;

    @Column(name = "iExpiratDateCalcu")
    private Short iExpiratDateCalcu;

    @Column(name = "cExpirationdate", length = 10)
    private String cExpirationdate;

    @Column(name = "dExpirationdate")
    private LocalDateTime dExpirationdate;

    @Column(name = "ipeqty", nullable = false, precision = 38, scale = 6)
    private BigDecimal ipeqty = BigDecimal.ZERO;

    @Column(name = "ipenum", nullable = false, precision = 38, scale = 6)
    private BigDecimal ipenum = BigDecimal.ZERO;
}