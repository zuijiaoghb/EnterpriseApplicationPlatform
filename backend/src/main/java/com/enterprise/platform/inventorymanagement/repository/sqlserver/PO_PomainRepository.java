package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Pomain;

import java.util.List;

@Repository
public interface PO_PomainRepository extends JpaRepository<PO_Pomain, Integer> {
    PO_Pomain findBycPOID(String cPOID);
    List<PO_Pomain> findByCVenCodeAndCAuditDateIsNotNull(String cVenCode);
}