package com.xanqus.security.repository;

import com.xanqus.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);// Jpa Query methods
}
