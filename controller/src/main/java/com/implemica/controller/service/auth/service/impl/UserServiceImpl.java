package com.implemica.controller.service.auth.service.impl;

import com.implemica.model.auth.entity.User;
import com.implemica.model.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements {@link UserDetailsService} and which loads user-specific data.
 * To do this, use the user DAO.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    /**
     * User DAO
     */
    private final UserRepository userRepository;

    /**
     * Loads user data from database by username and convert all in {@link UserDetails} object
     *
     * @param username the username identifying the user whose data is required.
     * @return  {@link UserDetails} object with user data
     * @throws UsernameNotFoundException if it cannot find user by username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found in data base"));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPassword(), authorities);
    }
}
