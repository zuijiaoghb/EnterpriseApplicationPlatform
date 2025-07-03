package com.enterprise.platform.inventorymanagement.model.sqlserver;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rdrecord10_extradefine")
public class Rdrecord10Extradefine {
    @Id
    private Long ID;

    @Column(name = "chdefine2")
    private String chdefine2;

    @OneToOne
    @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Rdrecord10 rdrecord10;
}