package com.implemica.application.controller.service.auth.service.impl;

import com.implemica.controller.service.auth.service.impl.UserServiceImpl;
import com.implemica.model.auth.entity.Role;
import com.implemica.model.auth.entity.User;
import com.implemica.model.auth.repository.RoleRepository;
import com.implemica.model.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private static UserRepository userRepository;

    @Mock
    private static RoleRepository roleRepository;

    private static UserServiceImpl userService;

    private static User testUser;

    private static Role admin_role;

    private static Role user_role;

    @BeforeEach
    void setUp() {

        userService = new UserServiceImpl(userRepository, roleRepository);

        testUser = User.builder().
                username("User 1").
                password("1029384756kkfkffpofpp_").
                roles(new ArrayList<>()).
                build();

        testUser.getRoles().add( Role.builder().id(12L).name("Role 1").build());
        testUser.getRoles().add( Role.builder().id(13L).name("Role 2").build());
        testUser.getRoles().add( Role.builder().id(14L).name("Role 3").build());

        admin_role = Role.builder().name("ADMIN_ROLE").build();
        user_role = Role.builder().name("USER_ROLE").build();
    }

    @Test
    void saveUser() {

        testUser.setPassword(new BCryptPasswordEncoder().encode(testUser.getPassword()));

        when(userRepository.save(testUser)).thenReturn(testUser);
        User user = userService.saveUser(testUser);
        assertEquals(testUser, user);
        verify(userRepository, times(1)).save(testUser);

        testUser = null;
        assertThatThrownBy(() -> userService.saveUser(testUser)).
                isInstanceOf(NullPointerException.class);
    }

    @Test
    void saveRole() {

        when(roleRepository.save(user_role)).thenReturn(user_role);
        Role role = userService.saveRole(user_role);
        assertEquals(user_role, role);
        verify(roleRepository, times(1)).save(user_role);

        when(roleRepository.save(admin_role)).thenReturn(admin_role);
        role = userService.saveRole(admin_role);
        assertEquals(admin_role, role);
        verify(roleRepository, times(1)).save(admin_role);
    }

    @Test
    void saveRole_null_role() {

        admin_role = null;
        assertThatThrownBy(() -> userService.saveRole(admin_role)).
                isInstanceOf(NullPointerException.class);
        verify(roleRepository, times(1)).save(admin_role);
    }

    @Test
    void addRoleToUser() {

        String username = testUser.getUsername();
        String roleName = admin_role.getName();

        when(userRepository.findAdminByUsername(username)).thenReturn(Optional.of(testUser));
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(admin_role));

        userService.addRoleToUser(username, roleName);

        verify(userRepository, times(1)).findAdminByUsername(username);
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    void addRoleToUser_not_found_role() {

        String username = testUser.getUsername();
        String roleName = admin_role.getName();

        when(userRepository.findAdminByUsername(username)).thenReturn(Optional.of(testUser));
        Mockito.lenient().when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.addRoleToUser(username, roleName)).
                isInstanceOf(NoSuchElementException.class).
                hasMessageContaining("No value present");

        verify(userRepository, times(1)).findAdminByUsername(username);
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    void addRoleToUser_not_found_user() {

        String username = testUser.getUsername();
        String roleName = admin_role.getName();

        when(userRepository.findAdminByUsername(username)).thenReturn(Optional.empty());
        Mockito.lenient().when(roleRepository.findByName(roleName)).thenReturn(Optional.of(admin_role));

        assertThatThrownBy(() -> userService.addRoleToUser(username, roleName)).
                isInstanceOf(UsernameNotFoundException.class).
                hasMessageContaining("User not found in data base");

        verify(userRepository, times(1)).findAdminByUsername(username);
        verify(roleRepository, times(0)).findByName(roleName);
    }

    @Test
    void getUsers() {

        List<User> exceptedUsers = List.of(
                User.builder().username("user 1").password("password 1").build(),
                User.builder().username("user 2").password("password 2").build(),
                User.builder().username("user 3").password("password 3").build()
        );

        when(userRepository.findAll()).thenReturn(exceptedUsers);
        List<User> users = userService.getUsers();
        assertEquals(exceptedUsers, users);

        exceptedUsers = List.of();
        when(userRepository.findAll()).thenReturn(exceptedUsers);
        users = userService.getUsers();
        assertEquals(exceptedUsers, users);

        verify(userRepository, times(2)).findAll();
    }

    @Test
    void getUser() {

        String userName = testUser.getUsername();

        when(userRepository.findAdminByUsername(userName)).thenReturn(Optional.of(testUser));
        User user = userService.getUser(userName);
        assertEquals(testUser, user);
        verify(userRepository, times(1)).findAdminByUsername(userName);
    }

    @Test
    void getUser_not_found_user() {

        String username = testUser.getUsername();

        when(userRepository.findAdminByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(username)).
                isInstanceOf(UsernameNotFoundException.class).
                hasMessageContaining("User not found in data base");

        verify(userRepository, times(1)).findAdminByUsername(username);
    }


    @Test
    void getUser_null_username() {

        when(userRepository.findAdminByUsername(null)).thenThrow(IllegalArgumentException.class);
        assertThatThrownBy(() -> userService.getUser(null)).
                isInstanceOf(IllegalArgumentException.class);
        verify(userRepository, times(1)).findAdminByUsername(null);
    }

    @Test
    void loadUserByUsername() {

        String username = testUser.getUsername();
        when(userRepository.findAdminByUsername(username)).thenReturn(Optional.of(testUser));

        UserDetails user = userService.loadUserByUsername(username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        testUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        UserDetails testUserDetails = new org.springframework.security.core.userdetails.
                User(testUser.getUsername(), testUser.getPassword(), authorities);

        assertEquals(testUserDetails, user);

        verify(userRepository, times(1)).findAdminByUsername(username);
    }

    @Test
    void loadUserByUsername_not_found_user() {

        String username = testUser.getUsername();

        when(userRepository.findAdminByUsername(username)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.loadUserByUsername(username)).
                isInstanceOf(UsernameNotFoundException.class).
                hasMessageContaining("User not found in data base");

        verify(userRepository, times(1)).findAdminByUsername(username);
    }
}