package com.enterprise.platform.inventorymanagement.model.sqlserver;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mom_orderdetail")
@DynamicInsert
@DynamicUpdate
public class MomOrderdetail {

    @Id
    @Column(name = "MoDId")
    private Integer moDId;

    @ManyToOne
    @JoinColumn(name = "MoId", nullable = false)
    private MomOrder momOrder;

    @Column(name = "SortSeq", nullable = false)
    private Integer sortSeq;

    @Column(name = "MoClass")
    private Byte moClass;

    @Column(name = "MoTypeId")
    private Integer moTypeId;

    @Column(name = "Qty")
    private BigDecimal qty;

    @Column(name = "MrpQty")
    private BigDecimal mrpQty;

    @Column(name = "AuxUnitCode")
    private String auxUnitCode;

    @Column(name = "AuxQty")
    private BigDecimal auxQty;

    @Column(name = "ChangeRate")
    private BigDecimal changeRate;

    @Column(name = "MoLotCode")
    private String moLotCode;

    @Column(name = "WhCode")
    private String whCode;

    @Column(name = "MDeptCode")
    private String mDeptCode;

    @Column(name = "SoType")
    private Byte soType;

    @Column(name = "SoDId")
    private String soDId;

    @Column(name = "SoCode")
    private String soCode;

    @Column(name = "SoSeq")
    private Integer soSeq;

    @Column(name = "DeclaredQty")
    private BigDecimal declaredQty;

    @Column(name = "QualifiedInQty")
    private BigDecimal qualifiedInQty;

    @Column(name = "Status")
    private Byte status;

    @Column(name = "OrgStatus")
    private Byte orgStatus;

    @Column(name = "BomId")
    private Integer bomId;

    @Column(name = "RoutingId")
    private Integer routingId;

    @Column(name = "CustBomId")
    private Integer custBomId;

    @Column(name = "DemandId")
    private Integer demandId;

    @Column(name = "PlanCode")
    private String planCode;

    @Column(name = "PartId")
    private Integer partId;

    @Column(name = "InvCode")
    private String invCode;

    @Column(name = "Free1")
    private String free1;

    @Column(name = "Free2")
    private String free2;

    @Column(name = "Free3")
    private String free3;

    @Column(name = "Free4")
    private String free4;

    @Column(name = "Free5")
    private String free5;

    @Column(name = "Free6")
    private String free6;

    @Column(name = "Free7")
    private String free7;

    @Column(name = "Free8")
    private String free8;

    @Column(name = "Free9")
    private String free9;

    @Column(name = "Free10")
    private String free10;

    @Column(name = "SfcFlag")
    private Boolean sfcFlag;

    @Column(name = "CrpFlag")
    private Boolean crpFlag;

    @Column(name = "QcFlag")
    private Boolean qcFlag;

    @Column(name = "RelsDate")
    private LocalDateTime relsDate;

    @Column(name = "RelsUser")
    private String relsUser;

    @Column(name = "CloseDate")
    private LocalDateTime closeDate;

    @Column(name = "OrgClsDate")
    private LocalDateTime orgClsDate;

    @Version
    @Column(name = "Ufts",insertable = false,updatable = false)
    private byte[] ufts;

    @Column(name = "Define22")
    private String define22;

    @Column(name = "Define23")
    private String define23;

    @Column(name = "Define24")
    private String define24;

    @Column(name = "Define25")
    private String define25;

    @Column(name = "Define26")
    private Double define26;

    @Column(name = "Define27")
    private Double define27;

    @Column(name = "Define28")
    private String define28;

    @Column(name = "Define29")
    private String define29;

    @Column(name = "Define30")
    private String define30;

    @Column(name = "Define31")
    private String define31;

    @Column(name = "Define32")
    private String define32;

    @Column(name = "Define33")
    private String define33;

    @Column(name = "Define34")
    private Integer define34;

    @Column(name = "Define35")
    private Integer define35;

    @Column(name = "Define36")
    private LocalDateTime define36;

    @Column(name = "Define37")
    private LocalDateTime define37;

    @Column(name = "LeadTime")
    private Integer leadTime;

    @Column(name = "OpScheduleType")
    private Byte opScheduleType;

    @Column(name = "OrdFlag")
    private Boolean ordFlag;

    @Column(name = "WIPType")
    private Byte wipType;

    @Column(name = "SupplyWhCode")
    private String supplyWhCode;

    @Column(name = "ReasonCode")
    private String reasonCode;

    @Column(name = "IsWFControlled")
    private Byte isWFControlled;

    @Column(name = "iVerifyState")
    private Integer iVerifyState;

    @Column(name = "iReturnCount")
    private Integer iReturnCount;

    @Column(name = "Remark")
    private String remark;

    @Column(name = "SourceMoCode")
    private String sourceMoCode;

    @Column(name = "SourceMoSeq")
    private Integer sourceMoSeq;

    @Column(name = "SourceMoId")
    private Integer sourceMoId;

    @Column(name = "SourceMoDId")
    private Integer sourceMoDId;

    @Column(name = "SourceQCCode")
    private String sourceQCCode;

    @Column(name = "SourceQCId")
    private Integer sourceQCId;

    @Column(name = "SourceQCDId")
    private Integer sourceQCDId;

    @Column(name = "CostItemCode")
    private String costItemCode;

    @Column(name = "CostItemName")
    private String costItemName;

    @Column(name = "RelsTime")
    private LocalDateTime relsTime;

    @Column(name = "CloseUser")
    private String closeUser;

    @Column(name = "CloseTime")
    private LocalDateTime closeTime;

    @Column(name = "OrgClsTime")
    private LocalDateTime orgClsTime;

    @Column(name = "AuditStatus")
    private Byte auditStatus;

    @Column(name = "PAllocateId")
    private Integer pAllocateId;

    @Column(name = "DemandCode")
    private String demandCode;

    @Column(name = "CollectiveFlag")
    private Byte collectiveFlag;

    @Column(name = "OrderType")
    private Byte orderType;

    @Column(name = "OrderDId")
    private Integer orderDId;

    @Column(name = "OrderCode")
    private String orderCode;

    @Column(name = "OrderSeq")
    private Integer orderSeq;

    @Column(name = "ManualCode")
    private String manualCode;

    @Column(name = "ReformFlag")
    private Boolean reformFlag;

    @Column(name = "SourceQCVouchType")
    private Byte sourceQCVouchType;

    @Column(name = "OrgQty")
    private BigDecimal orgQty;

    @Column(name = "FmFlag")
    private Boolean fmFlag;

    @Column(name = "MinSN")
    private String minSN;

    @Column(name = "MaxSN")
    private String maxSN;

    @Column(name = "SourceSvcCode")
    private String sourceSvcCode;

    @Column(name = "SourceSvcId")
    private String sourceSvcId;

    @Column(name = "SourceSvcDId")
    private String sourceSvcDId;

    @Column(name = "BomType")
    private Byte bomType;

    @Column(name = "RoutingType")
    private Byte routingType;

    @Column(name = "BusFlowId")
    private Integer busFlowId;

    @Column(name = "RunCardFlag")
    private Boolean runCardFlag;

    @Column(name = "RequisitionFlag")
    private Boolean requisitionFlag;

    @Column(name = "AlloVTid")
    private Integer alloVTid;

    @Column(name = "RelsAlloVTid")
    private Integer relsAlloVTid;

    @Column(name = "iPrintCount")
    private Integer iPrintCount;

    @Column(name = "cbSysBarCode")
    private String cbSysBarCode;

    @Column(name = "cCurrentAuditor")
    private String cCurrentAuditor;

    @Column(name = "CustCode")
    private String custCode;

    @Column(name = "LPlanCode")
    private String lPlanCode;

    @Column(name = "SourceSvcVouchType")
    private Byte sourceSvcVouchType;

    // 关联生产订单表
    @OneToOne(mappedBy = "momOrderdetail")
    private MomMorder momMorder;
}