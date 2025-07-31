package com.enterprise.platform.user.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    private String password;
    private String email;
    private List<Long> roleIds; // 明确接收角色ID列表
    @NotBlank(message = "中文名称不能为空")
    @Size(max = 50, message = "中文名称不能超过50个字符")
    private String cnname; // 中文名称
    // 其他字段...
}
