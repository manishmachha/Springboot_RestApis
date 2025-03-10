package com.prep.restapis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prep.restapis.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods can be defined here
    User findByUsername(String username);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsById(int id);
}