package org.week7assignment.messenger.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.week7assignment.messenger.model.Message;

@Getter
@AllArgsConstructor
public class MessagePreviewResponseData {
    private final String id;
    private final String title;
    private final String senderEmail;

    public MessagePreviewResponseData(Message message) {
        this.id = message.getId().toString();
        this.title = message.getTitle();
        this.senderEmail = message.getSender().getEmail();
    }
}
