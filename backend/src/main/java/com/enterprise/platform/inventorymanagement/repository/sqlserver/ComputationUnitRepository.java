package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.ComputationUnit;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputationUnitRepository extends JpaRepository<ComputationUnit, String> {
    @Query("SELECT cu FROM ComputationUnit cu WHERE cu.cComunitCode IN :cComunitCodes")
    List<ComputationUnit> findByCComunitCodeIn(@Param("cComunitCodes") Set<String> cComunitCodes);
}
