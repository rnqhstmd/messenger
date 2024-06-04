package org.week7assignment.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.week7assignment.messenger.model.UserMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, UUID> {
    Optional<UserMessage> findByReceiverIdAndMessageId(UUID receiverId, UUID messageId);
    List<UserMessage> findByReceiverId(UUID receiverId);
    boolean existsByMessageIdAndReadAtIsNotNull(UUID messageId);
}
