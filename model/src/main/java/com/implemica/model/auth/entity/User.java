package com.implemica.model.auth.entity;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The domain model of the user that is used to work with the user data
 * and the security application part. Contains user authorization data
 * uch as username and password that are used to authenticate and verify
 * the useres, as well as its list of roles, depending on which it will
 * be given the right to perform certain actions and which will be
 * written in JWT token
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder
public class User {

    /**
     * User primary key that used to search the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /**
     * User username that used to load from database by
     * {@link UserDetailsService#loadUserByUsername(String)} method.
     */
    @Column(name = "username")
    private String username;

    /**
     * Username password used to authenticate the user when attempting to
     * authenticate and receive JWT token to execute protected requests.
     */
    @Column(name = "password")
    private String password;

    /**
     * Lists the roles that the User can perform certain queries based on.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles= new ArrayList<>();
}
