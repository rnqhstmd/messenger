package org.week7assignment.messenger.dto.response.message;

import lombok.Getter;
import org.week7assignment.messenger.model.Message;

@Getter
public class MessageResponseData {

    private final String id;
    private final String title;
    private final String content;
    private final String senderEmail;

    public MessageResponseData(Message message) {
        this.id = message.getId().toString();
        this.title = message.getTitle();
        this.content = message.getContent();
        this.senderEmail = message.getSender().getEmail();
    }
}
