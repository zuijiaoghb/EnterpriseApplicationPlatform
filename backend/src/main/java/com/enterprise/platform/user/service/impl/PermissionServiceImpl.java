package com.enterprise.platform.user.service.impl;

import com.enterprise.platform.user.model.Permission;
import com.enterprise.platform.user.repository.PermissionRepository;
import com.enterprise.platform.user.service.PermissionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(transactionManager = "mysqlTransactionManager")
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {    
    
    private final PermissionRepository permissionRepository;

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission update(Long id, Permission permission) {
        permission.setId(id);
        return permissionRepository.save(permission);
    }

    @Override
    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public List<Permission> getPermissionTree() {
        List<Permission> roots = permissionRepository.findByParentIsNull();
        return roots.stream()
            .map(this::buildTree)
            .collect(Collectors.toList());
    }

    private Permission buildTree(Permission root) {
        Set<Permission> children = permissionRepository.findByParent(root);
        if (children != null && !children.isEmpty()) {
            for (Permission child : children) {
                if (!child.getId().equals(root.getId())) {  // 防止循环引用
                    buildTree(child);
                }
            }
            root.setChildren(children);
        }
        return root;
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public List<Permission> findAllRootPermissions() {
        List<Permission> roots = permissionRepository.findByParentIsNull();
        return roots.stream()
            .map(root -> {
                Permission tree = new Permission();
                tree.setId(root.getId());
                tree.setName(root.getName());
                tree.setCode(root.getCode());
                tree.setDescription(root.getDescription());
                tree.setChildren(buildChildren(root));
                return tree;
            })
            .collect(Collectors.toList());
    }

    private Set<Permission> buildChildren(Permission parent) {
        Set<Permission> children = permissionRepository.findByParent(parent);
        return children.stream()
            .map(child -> {
                Permission node = new Permission();
                node.setId(child.getId());
                node.setName(child.getName());
                node.setCode(child.getCode());
                node.setDescription(child.getDescription());
                node.setChildren(buildChildren(child));
                return node;
            })
            .collect(Collectors.toSet());
    }
}
