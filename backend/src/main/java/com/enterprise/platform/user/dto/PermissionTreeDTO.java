package com.enterprise.platform.user.dto;

import java.util.List;

import lombok.Data;

@Data
public class PermissionTreeDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private List<PermissionTreeDTO> children;
}
