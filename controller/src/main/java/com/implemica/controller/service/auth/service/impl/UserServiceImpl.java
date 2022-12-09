package com.implemica.controller.service.auth.service.impl;

import com.implemica.controller.service.auth.service.UserService;
import com.implemica.model.auth.entity.Role;
import com.implemica.model.auth.entity.User;
import com.implemica.model.auth.repository.RoleRepository;
import com.implemica.model.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    @Override
    public User saveUser(User user) {

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        User user_ = userRepository.save(user);
        log.info("Saving new user {} to data base", user.getUsername());
        return user_;
    }

    @Override
    public Role saveRole(Role role) {

        Role role_ = roleRepository.save(role);

        log.info("Saving new role {} to data base", role.getName());
        return role_;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

        User user = userRepository.findAdminByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found in data base"));

        Role role = roleRepository.findByName(roleName).orElseThrow();
        user.getRoles().add(role);

        log.info("Adding new role {} for user {}", role.getName(), user.getUsername());
    }

    @Override
    public List<User> getUsers() {

        List<User> users = userRepository.findAll();
        log.info("Fetching all users");
        return users;
    }

    @Override
    public User getUser(String username) {

        User user = userRepository.findAdminByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found in data base"));

        log.info("Fetching user {}", username);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findAdminByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found in data base"));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPassword(), authorities);
    }
}
