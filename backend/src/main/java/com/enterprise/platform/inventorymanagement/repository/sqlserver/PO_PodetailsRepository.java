package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Podetails;

import java.util.List;

@Repository
public interface PO_PodetailsRepository extends JpaRepository<PO_Podetails, Integer> {
    List<PO_Podetails> findByPoPomainPoid(Integer poid);
}