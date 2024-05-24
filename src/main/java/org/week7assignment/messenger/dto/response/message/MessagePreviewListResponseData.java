package org.week7assignment.messenger.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.week7assignment.messenger.model.Message;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MessagePreviewListResponseData {
    private final List<MessagePreviewResponseData> messages;
    private final int total;

    public MessagePreviewListResponseData(List<Message> messageList) {
        this.messages = messageList.stream().map(message -> new MessagePreviewResponseData(message)).collect(Collectors.toList());
        this.total = messageList.size();
    }
}