package org.week7assignment.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.week7assignment.messenger.model.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findBySenderId(UUID userId);
    Optional<Message> findByIdAndSenderId(UUID messageId, UUID userId);
}
