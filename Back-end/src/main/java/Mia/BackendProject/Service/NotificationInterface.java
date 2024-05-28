package Mia.BackendProject.Service;

import Mia.BackendProject.Entity.User;
import Mia.BackendProject.Entity.Notification;
import org.springframework.http.ResponseEntity;

public interface NotificationInterface {
    Notification createNotification(User Sender,User recipient, String message);
     ResponseEntity<?> getNotificationsForUser(String idUser);
    ResponseEntity<?> markNotificationAsRead(String recipientId,String userFromId,String typeNotification,long idPost);
    public Notification markNotificationAsUnread(Long notificationId);

    }