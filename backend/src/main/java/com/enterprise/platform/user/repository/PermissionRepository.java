package com.enterprise.platform.user.repository;

import com.enterprise.platform.user.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    List<Permission> findByParentIsNull();
    
    Set<Permission> findByParent(Permission parent);
}
