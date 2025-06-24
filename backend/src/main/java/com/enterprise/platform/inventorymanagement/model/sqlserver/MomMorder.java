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
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mom_morder")
@DynamicInsert
@DynamicUpdate
public class MomMorder {

    @Id
    @Column(name = "MoDId")
    private Integer moDId;

    @OneToOne
    @PrimaryKeyJoinColumn
    private MomOrderdetail momOrderdetail;

    @ManyToOne
    @JoinColumn(name = "MoId")
    private MomOrder momOrder;

    @Column(name = "StartDate")
    private LocalDateTime startDate;

    @Column(name = "DueDate")
    private LocalDateTime dueDate;

    @Version
    @Column(name = "Ufts",insertable = false,updatable = false)
    private byte[] ufts;
}