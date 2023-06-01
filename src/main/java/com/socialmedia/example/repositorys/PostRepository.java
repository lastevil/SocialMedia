package com.socialmedia.example.repositorys;

import com.socialmedia.example.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByUserId(@Param("user_id") UUID userId);
}
