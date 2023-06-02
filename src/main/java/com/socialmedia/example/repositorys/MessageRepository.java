package com.socialmedia.example.repositorys;

import com.socialmedia.example.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    @Query(value = "select * from " +
            "(select * from messages where sender_id=:senderId and receiver_id=:friend " +
            " union" +
            " select * from messages where sender_id=:friend and receiver_id=:senderId) as \"result\" " +
            " order by created_at asc", nativeQuery = true)
    List<Message> findAllMessagesBetweenFriends(@Param(value = "senderId") UUID senderId, @Param(value = "friend") UUID friend);
}
