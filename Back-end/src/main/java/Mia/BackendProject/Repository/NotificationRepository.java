package Mia.BackendProject.Repository;

import Mia.BackendProject.Entity.Notification;
import Mia.BackendProject.Entity.User;
import Mia.BackendProject.Entity.enm.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);

    Optional<Notification> findByRecipientAndUserFromAndNotificationType(User recipient, User userFrom, NotificationType notificationType);

    Optional<Notification> findByRecipientAndUserFromAndNotificationTypeAndIdPost(User recipient, User userFrom, NotificationType notificationType,long idPost);




}
