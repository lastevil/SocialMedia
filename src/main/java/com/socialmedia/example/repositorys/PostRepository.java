package com.socialmedia.example.repositorys;

import com.socialmedia.example.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query("select p from Post p " +
            "where p.user.id in " +
            "(select s.receiver.id from Subscriber s where s.sender.id=:userId)")
    Page<Post> findByUserId(@Param("userId")UUID id, Pageable pageable);

    List<Post> findAllByUserId(@Param("user_id") UUID userId);
}
