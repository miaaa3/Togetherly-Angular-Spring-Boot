package Mia.BackendProject.Service.Implement;

import Mia.BackendProject.Entity.ChatMessage;
import Mia.BackendProject.Entity.Notification;
import Mia.BackendProject.Entity.User;
import Mia.BackendProject.Entity.enm.MessageType;
import Mia.BackendProject.Entity.enm.NotificationType;
import Mia.BackendProject.Repository.ChatMessageRepository;
import Mia.BackendProject.Repository.NotificationRepository;
import Mia.BackendProject.Repository.UserRepository;
import Mia.BackendProject.Service.ChatMessageInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ChatMessageService implements ChatMessageInterface {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage createMessage(String userFromId, String recipientId, String content, String typeMessage ) {

        try {
            User recipient = userRepository.findById(recipientId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            User sender = userRepository.findById(userFromId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setRecipient(recipient);
            chatMessage.setSender(sender);
            chatMessage.setType(MessageType.valueOf(typeMessage));
            chatMessage.setContent(content);
            if (!Objects.equals(sender.getId(), recipient.getId())){
                if (notificationRepository.findByRecipientAndUserFromAndNotificationType(recipient,sender, NotificationType.MESSAGE).isPresent()){
                    notificationRepository.deleteById(notificationRepository.findByRecipientAndUserFromAndNotificationType(recipient,sender,NotificationType.MESSAGE).get().getId());
                }
                Notification notification=new Notification();
                notification.setUserFrom(sender);
                notification.setIsRead(false);
                notification.setRecipient(recipient);
                notification.setMessage(sender.getFirstName()+" "+sender.getLastName()+" send your a message");
                notification.setNotificationType(NotificationType.MESSAGE);
                notification.setEmailFrom(sender.getEmail());
                notificationRepository.save(notification);
            }

            return chatMessageRepository.save(chatMessage);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public ResponseEntity<?> getMessageBySenderAndRecipient(String userFromId, String recipientId){
        try{
            User recipient = userRepository.findById(recipientId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            User sender = userRepository.findById(userFromId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            return ResponseEntity.ok(chatMessageRepository.findAllBySenderAndRecipientOrSenderAndRecipientOrderByCreatedAt(sender,recipient,recipient,sender));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public void deleteMessage(Long messageId) {
        chatMessageRepository.deleteById(messageId);
    }
}
