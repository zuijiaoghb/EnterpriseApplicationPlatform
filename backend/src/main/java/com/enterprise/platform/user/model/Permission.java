package com.enterprise.platform.user.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "permissions") // 明确指定表名
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false) // 添加非空约束
    private String name;
    
    @Column(nullable = false, unique = true) // 添加唯一约束
    private String code;
    
    @Column(length = 500) // 增加描述字段长度
    private String description;
    
    @Column(name = "parent_id", insertable = false, updatable = false) // 修改为只读
    private Long parentId;
    
    // 添加审计字段
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 添加预持久化方法
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore  // 避免父权限序列化时循环引用
    private Permission parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Permission> children = new HashSet<>();

    // 添加父权限设置方法
    public void setParent(Permission parent) {
        this.parent = parent;
        this.parentId = parent != null ? parent.getId() : null;
    }

    // 添加子权限方法
    public void addChild(Permission child) {
        children.add(child);
        child.setParent(this);
    }
}
