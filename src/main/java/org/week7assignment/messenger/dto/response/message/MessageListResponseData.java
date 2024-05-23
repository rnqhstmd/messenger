package org.week7assignment.messenger.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.week7assignment.messenger.model.Message;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MessageListResponseData {
    private final List<MessageResponseData> messages;
    private final int total;

    public MessageListResponseData(List<Message> messageList) {
        this.messages = messageList.stream().map(message -> new MessageResponseData(message)).collect(Collectors.toList());
        this.total = messageList.size();
    }
}