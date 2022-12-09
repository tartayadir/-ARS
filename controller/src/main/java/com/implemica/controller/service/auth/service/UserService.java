package com.implemica.controller.service.auth.service;

import com.implemica.model.auth.entity.User;
import com.implemica.model.auth.entity.Role;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    List<User> getUsers();
    User getUser(String username);
}
