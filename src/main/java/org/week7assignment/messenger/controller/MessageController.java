package org.week7assignment.messenger.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week7assignment.messenger.authentication.annotation.OrganizationMember;
import org.week7assignment.messenger.dto.request.message.MessageCreateDto;
import org.week7assignment.messenger.dto.request.message.MessageReplyDto;
import org.week7assignment.messenger.dto.request.message.MessageUpdateDto;
import org.week7assignment.messenger.dto.response.ResponseDto;
import org.week7assignment.messenger.dto.response.message.MessageListResponseData;
import org.week7assignment.messenger.dto.response.message.MessageResponseData;
import org.week7assignment.messenger.model.User;
import org.week7assignment.messenger.service.MessageService;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    // 메세지 보내기
    @PostMapping
    public ResponseEntity<ResponseDto<MessageResponseData>> sendMessage(@OrganizationMember User user,
                                                                        @Valid @RequestBody MessageCreateDto messageCreateDto) {
        MessageResponseData messageResponseData = messageService.send(user, messageCreateDto);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.CREATED, "메세지 전송 완료", messageResponseData), HttpStatus.CREATED);
    }

    // 메세지 읽기
    @PostMapping("/{id}")
    public ResponseEntity<ResponseDto<MessageResponseData>> readMessage(@OrganizationMember User user,
                                                                        @PathVariable("id") UUID messageId) {
        MessageResponseData messageResponseData = messageService.readMessage(user, messageId);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "메세지 읽기 완료", messageResponseData), HttpStatus.OK);
    }

    // 보낸 메세지 목록 조회
    @GetMapping("/sent")
    public ResponseEntity<ResponseDto<MessageListResponseData>> getSentMessages(@OrganizationMember User user) {
        MessageListResponseData sentMessages = messageService.getSentMessages(user);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "보낸 메세지 조회 완료", sentMessages), HttpStatus.OK);
    }

    // 받은 메세지 목록 조회
    @GetMapping("/received")
    public ResponseEntity<ResponseDto<MessageListResponseData>> getReceivedMessages(@OrganizationMember User user) {
        MessageListResponseData receivedMessages = messageService.getReceivedMessages(user);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "받은 메세지 조회 완료", receivedMessages), HttpStatus.OK);
    }

    // 메세지 답장
    @PostMapping("/{id}/reply")
    public ResponseEntity<ResponseDto<MessageResponseData>> replyToMessage(@OrganizationMember User user,
                                                                           @PathVariable("id") UUID messageId,
                                                                           @Valid @RequestBody MessageReplyDto messageReplyDto) {
        MessageResponseData messageResponseData = messageService.replyToMessage(user, messageId, messageReplyDto);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "답장 완료", messageResponseData), HttpStatus.OK);
    }

    // 메세지 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto<MessageResponseData>> updateMessage(@OrganizationMember User user,
                                                                          @PathVariable("id") UUID messageId,
                                                                          @Valid @RequestBody MessageUpdateDto messageUpdateDto) {
        MessageResponseData messageResponseData = messageService.updateMessage(user, messageId, messageUpdateDto);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "메세지 수정 완료", messageResponseData), HttpStatus.OK);
    }

    // 메세지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteMessage(@OrganizationMember User user,
                                                           @PathVariable("id") UUID messageId) {
        messageService.deleteMessage(user, messageId);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "메세지 삭제 완료"), HttpStatus.OK);
    }
}
