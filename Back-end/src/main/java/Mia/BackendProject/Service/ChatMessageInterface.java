package Mia.BackendProject.Service;

import Mia.BackendProject.Entity.ChatMessage;
import org.springframework.http.ResponseEntity;

public interface ChatMessageInterface {
    ChatMessage createMessage(String userFromId, String recipientId, String content,String typeMessage);

    void deleteMessage(Long messageId);

    ResponseEntity<?> getMessageBySenderAndRecipient(String userFromId, String recipientId);

}
