package Mia.BackendProject.Service.Implement;

import Mia.BackendProject.Entity.Notification;
import Mia.BackendProject.Entity.User;
import Mia.BackendProject.Entity.enm.NotificationType;
import Mia.BackendProject.Repository.NotificationRepository;
import Mia.BackendProject.Repository.UserRepository;
import Mia.BackendProject.Service.NotificationInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationInterface {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Notification createNotification(User sender, User recipient, String message) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setUserFrom(sender);
        notification.setMessage(message);
        notification.setIsRead(false);
        return notification;
    }

    @Override
    public ResponseEntity<?> getNotificationsForUser(String idUser) {
        try{
            User user = userRepository.findById(idUser)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            return ResponseEntity.ok(notificationRepository.findByRecipientOrderByCreatedAtDesc(user)) ;
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
    @Override
    public ResponseEntity<?> markNotificationAsRead(String recipientId,String userFromId,String typeNotification,long idPost) {
        try{
            User recipient = userRepository.findById(recipientId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            User userFrom = userRepository.findById(userFromId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            Notification notification;
            if ( idPost!= -1){
                notification = notificationRepository.findByRecipientAndUserFromAndNotificationTypeAndIdPost(recipient, userFrom, NotificationType.valueOf(typeNotification), idPost).orElseThrow(ChangeSetPersister.NotFoundException::new);
            }else {
                notification = notificationRepository.findByRecipientAndUserFromAndNotificationType(recipient, userFrom, NotificationType.valueOf(typeNotification)).orElseThrow(ChangeSetPersister.NotFoundException::new);;
            }
            notification.setIsRead(true);
            notificationRepository.save(notification);

            return ResponseEntity.ok("Has been read") ;
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @Override
    public Notification markNotificationAsUnread(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setIsRead(false);
            return notificationRepository.save(notification);
        }
        return null;
    }
}
