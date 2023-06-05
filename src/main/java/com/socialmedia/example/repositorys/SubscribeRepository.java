package com.socialmedia.example.repositorys;

import com.socialmedia.example.entities.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscriber, Long> {
    List<Subscriber> findBySender_IdAndIsFriends(UUID id, boolean isFriends);
    Optional<Subscriber> findBySender_IdAndReceiver_Id(UUID senderId, UUID receiverId);
    boolean existsBySender_IdAndReceiver_Id(UUID senderId, UUID receiverId);
}
