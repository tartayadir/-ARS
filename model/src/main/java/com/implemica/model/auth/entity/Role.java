package com.implemica.model.auth.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Domain model of user role that contains name and role id in database.
 * It determines rights of user and what actions he can perform. It is
 * used in the security configuration class to provide the ability to
 * execute specific requests on protected endpoints with allowed HTTP methods.
 * Also, the user’s role information is stored in a token to further determine
 * the permitted actions.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@Builder
public class Role {

    /**
     * Role primary key that used to search the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    /**
     * The name of the role according to which the user gets
     * the right to perform actions.
     */
    @Column(name="name")
    private String name;
}
