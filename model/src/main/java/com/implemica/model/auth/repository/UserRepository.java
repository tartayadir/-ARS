package com.implemica.model.auth.repository;

import com.implemica.model.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO for custom user implementation entity {@link User} that use
 * id type {@link Long}
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

     /**
      * Returns user with provided username form database
      *
      * @param username username
      * @return user from database
      */
     Optional<User> findUserByUsername(String username);
}
