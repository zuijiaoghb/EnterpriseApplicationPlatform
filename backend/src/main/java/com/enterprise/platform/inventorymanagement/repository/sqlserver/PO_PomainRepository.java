package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Pomain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PO_PomainRepository extends JpaRepository<PO_Pomain, Integer> {
    PO_Pomain findBycPOID(String cPOID);
    @Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER(ORDER BY dPODate DESC) AS rn FROM PO_Pomain WHERE cVenCode = :cVenCode AND cAuditDate IS NOT NULL) AS sub WHERE rn > :offset AND rn <= :offset + :pageSize",
           nativeQuery = true)
    List<PO_Pomain> findByCVenCodeAndCAuditDateIsNotNull(@Param("cVenCode") String cVenCode,
                                                          @Param("offset") int offset,
                                                          @Param("pageSize") int pageSize);

    @Query(value = "SELECT COUNT(*) FROM PO_Pomain WHERE cVenCode = :cVenCode AND cAuditDate IS NOT NULL", nativeQuery = true)
    long countByCVenCodeAndCAuditDateIsNotNull(@Param("cVenCode") String cVenCode);
}