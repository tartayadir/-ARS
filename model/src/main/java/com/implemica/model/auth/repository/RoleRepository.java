package com.implemica.model.auth.repository;

import com.implemica.model.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO for custom role implementation entity {@link Role} that use
 * id type {@link Long}
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Returns role with provided role name form database
     *
     * @param name role name
     * @return role from database
     */
    Optional<Role> findByName(String name);
}
