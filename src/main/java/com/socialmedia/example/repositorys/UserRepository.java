package com.socialmedia.example.repositorys;

import com.socialmedia.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    Boolean existsUserByUsername(String username);

    User findByEmail(String email);
}
