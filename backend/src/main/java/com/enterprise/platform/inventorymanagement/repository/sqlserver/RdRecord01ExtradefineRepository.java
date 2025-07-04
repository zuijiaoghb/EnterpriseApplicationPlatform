package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.RdRecord01Extradefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RdRecord01ExtradefineRepository extends JpaRepository<RdRecord01Extradefine, Long> {
}