package com.enterprise.platform.inventorymanagement.model.sqlserver;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "RdRecord01_extradefine")
public class RdRecord01Extradefine {
    @Id
    private Long ID;

    @OneToOne
    @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private RdRecord01 rdRecord01;

    @Column(name = "chdefine1")
    private String chdefine1;

    @Column(name = "chdefine2")
    private String chdefine2;
}