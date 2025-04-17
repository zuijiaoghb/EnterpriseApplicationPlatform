package com.enterprise.platform.user.service;

import java.util.Set;

public interface AuditService {
    void logPermissionChange(String operator, Long roleId, Set<Long> permissionIds);
}
