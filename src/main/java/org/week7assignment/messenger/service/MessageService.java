package org.week7assignment.messenger.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.week7assignment.messenger.dto.request.message.MessageCreateDto;
import org.week7assignment.messenger.dto.request.message.MessageReplyDto;
import org.week7assignment.messenger.dto.request.message.MessageUpdateDto;
import org.week7assignment.messenger.dto.response.message.MessageListResponseData;
import org.week7assignment.messenger.dto.response.message.MessageResponseData;
import org.week7assignment.messenger.exception.ConflictException;
import org.week7assignment.messenger.exception.NotFoundException;
import org.week7assignment.messenger.exception.errorCode.ErrorCode;
import org.week7assignment.messenger.model.Message;
import org.week7assignment.messenger.model.User;
import org.week7assignment.messenger.model.UserMessage;
import org.week7assignment.messenger.repository.MessageRepository;
import org.week7assignment.messenger.repository.UserMessageRepository;
import org.week7assignment.messenger.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserMessageRepository userMessageRepository;

    // 메세지 보내기
    public MessageResponseData send(User sender, MessageCreateDto messageCreateDto) {
        // 보낼 메세지 생성
        Message message = Message.builder()
                .title(messageCreateDto.getTitle())
                .content(messageCreateDto.getContent())
                .sender(sender)
                .build();

        // 수신자 검증
        List<User> recipients = validRecipientsByUserId(messageCreateDto.getRecipients());
        log.info("recipients={}", recipients);
        List<UserMessage> userMessages = recipients.stream()
                .map(recipient -> UserMessage.builder()
                        .message(message)
                        .receiver(recipient)
                        .build()).collect(Collectors.toList());

        message.setUserMessages(userMessages);
        messageRepository.save(message);

        return new MessageResponseData(message);
    }

    private List<User> validRecipientsByUserId(List<UUID> userIds) {
        List<User> recipients = userRepository.findAllById(userIds);
        if (recipients.size() != userIds.size()) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }
        return recipients;
    }

    // 메세지 읽기
    public MessageResponseData readMessage(User user, UUID messageId) {
        UserMessage receivedMessage = userMessageRepository.findByReceiverIdAndMessageId(user.getId(), messageId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));

        // 메세지를 읽었을 때 시간 기록
        if (receivedMessage.getReadAt() == null) {
            receivedMessage.setReadAt(LocalDateTime.now());
            userMessageRepository.save(receivedMessage);
        }

        Message message = receivedMessage.getMessage();
        return new MessageResponseData(message);
    }

    // 보낸 메세지 목록 조회
    public MessageListResponseData getSentMessages(User user) {
        List<Message> messages = messageRepository.findBySenderId(user.getId());
        return new MessageListResponseData(messages);
    }

    // 받은 메세지 목록 조회
    public MessageListResponseData getReceivedMessages(User user) {
        List<UserMessage> userMessages = userMessageRepository.findByReceiverId(user.getId());
        List<Message> messageList = userMessages.stream().map(UserMessage::getMessage)
                .collect(Collectors.toList());
        return new MessageListResponseData(messageList);
    }

    // 메세지 답장
    public MessageResponseData replyToMessage(User user, UUID messageId, MessageReplyDto messageReplyDto) {
        // 읽은 것으로 간주
        UserMessage receivedMessage = userMessageRepository.findByReceiverIdAndMessageId(user.getId(), messageId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));

        if (receivedMessage.getReadAt() == null) {
            receivedMessage.setReadAt(LocalDateTime.now());
            userMessageRepository.save(receivedMessage);
        }

        Message originalMessage = validMessage(null, messageId);

        Message replyMessage = Message.builder()
                .title(messageReplyDto.getTitle())
                .content(messageReplyDto.getContent())
                .sender(user)
                .replyMessage(originalMessage)
                .build();
        messageRepository.save(replyMessage);

        return new MessageResponseData(replyMessage);
    }

    // 메세지 수정 (수신자가 읽지 않았을 시에만)
    public MessageResponseData updateMessage(User user, UUID messageId, MessageUpdateDto messageUpdateDto) {
        // 원본 메세지 가져오기
        Message message = validMessage(user, messageId);

        // 수신자가 읽었는지 확인
        if (userMessageRepository.existsByMessageIdAndReadAtIsNotNull(message.getId())) {
            throw new ConflictException(ErrorCode.MESSAGE_ALREADY_READ);
        }

        // 메세지 수정
        message.setTitle(messageUpdateDto.getTitle());
        message.setContent(messageUpdateDto.getContent());
        messageRepository.save(message);

        return new MessageResponseData(message);
    }

    // 메세지 삭제 (수신자가 읽지 않았을 시에만)
    public void deleteMessage(User user, UUID messageId) {
        // 원본 메세지 가져오기
        Message message = validMessage(user, messageId);

        // 수신자가 읽었는지 확인
        if (userMessageRepository.existsByMessageIdAndReadAtIsNotNull(message.getId())) {
            throw new ConflictException(ErrorCode.MESSAGE_ALREADY_READ);
        }

        messageRepository.delete(message);
    }

    private Message validMessage(User user, UUID messageId) {
        if (user != null) {
            return messageRepository.findByIdAndSenderId(messageId, user.getId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
        } else {
            return messageRepository.findById(messageId)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
        }
    }
}
