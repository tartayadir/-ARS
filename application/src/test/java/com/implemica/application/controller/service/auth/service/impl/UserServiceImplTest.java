package com.implemica.application.controller.service.auth.service.impl;

import com.implemica.controller.service.auth.service.impl.UserServiceImpl;
import com.implemica.model.auth.entity.Role;
import com.implemica.model.auth.entity.User;
import com.implemica.model.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@ActiveProfiles("prod")
@Slf4j
class UserServiceImplTest {

    @Mock
    private static UserRepository userRepository;

    private static UserServiceImpl userService;

    private static User testUser;

    @BeforeEach
    void setUp() {

        userService = new UserServiceImpl(userRepository);

        testUser = User.builder().
                username("User 1").
                password("1029384756kkfkffpofpp_").
                roles(new ArrayList<>()).
                build();

        testUser.getRoles().add( Role.builder().id(12L).name("Role 1").build());
        testUser.getRoles().add( Role.builder().id(13L).name("Role 2").build());
        testUser.getRoles().add( Role.builder().id(14L).name("Role 3").build());
    }

    @Test
    void loadUserByUsername() {

       checkLoadUserByUsername(testUser.getUsername(), testUser);
    }

    @Test
    void loadUserByUsername_not_found_user() {

        checkLoadUserByUsernameNotFoundUser(testUser.getUsername());
    }

    private static void checkLoadUserByUsername(String username, User user) {

        when(userRepository.findAdminByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userBD = userService.loadUserByUsername(username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        testUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        UserDetails testUserDetails = new org.springframework.security.core.userdetails.
                User(testUser.getUsername(), testUser.getPassword(), authorities);

        assertEquals(testUserDetails, userBD);

        verify(userRepository, times(1)).findAdminByUsername(username);
        reset(userRepository);
    }

    private static void checkLoadUserByUsernameNotFoundUser(String username) {

        when(userRepository.findAdminByUsername(username)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.loadUserByUsername(username)).
                isInstanceOf(UsernameNotFoundException.class).
                hasMessageContaining("User not found in data base");

        verify(userRepository, times(1)).findAdminByUsername(username);
        reset(userRepository);
    }
}