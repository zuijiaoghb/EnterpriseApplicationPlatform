package com.enterprise.platform.user.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    private String password;
    private String email;
    private List<Long> roleIds; // 明确接收角色ID列表
    // 其他字段...
}
