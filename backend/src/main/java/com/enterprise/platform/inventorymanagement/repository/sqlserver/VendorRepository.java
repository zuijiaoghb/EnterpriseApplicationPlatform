package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Vendor;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, String> {
    @Query("SELECT v FROM Vendor v WHERE v.cVenCode IN :cVenCodes")
    List<Vendor> findByCVenCodeIn(@Param("cVenCodes") Set<String> cVenCodes);
}