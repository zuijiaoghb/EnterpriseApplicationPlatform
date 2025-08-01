package com.enterprise.platform.user.service;

import com.enterprise.platform.user.dto.UserDTO;
import com.enterprise.platform.user.model.User;

import java.util.Set;

import org.springframework.data.domain.Page;

public interface UserService {
    Page<User> getAllUsers(int page, int size);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User updateUserRoles(Long id, Set<Long> roleIds);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username,Long excludeUserId);
    UserDTO getUserByUsername(String username);
    void changePassword(String username, String oldPassword, String newPassword);
}