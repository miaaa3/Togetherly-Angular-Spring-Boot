package Mia.BackendProject.Service.Implement;

import Mia.BackendProject.Dto.LikeDto;
import Mia.BackendProject.Entity.Like;
import Mia.BackendProject.Entity.Notification;
import Mia.BackendProject.Entity.Post;
import Mia.BackendProject.Entity.User;
import Mia.BackendProject.Entity.enm.NotificationType;
import Mia.BackendProject.Repository.LikeRepository;
import Mia.BackendProject.Repository.NotificationRepository;
import Mia.BackendProject.Repository.PostRepository;
import Mia.BackendProject.Repository.UserRepository;
import Mia.BackendProject.Service.LikeInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikeService implements LikeInterface {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private WebSocketService webSocketNotificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PostRepository postRepository;





    @Override
    public ResponseEntity<?> likeUnlikePost(LikeDto likeDto)  {
        try {
            User user = userRepository.findById(likeDto.getIdUser())
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            Post post = postRepository.findById(likeDto.getIdPost())
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            // Check if the user has already liked the post and if like unlike the post
            if (likeRepository.existsByPostAndUsers(post, user)) {
                long idPost=likeRepository.findIdByPostAndUsers(user.getId(),post.getId());
                likeRepository.deleteById(idPost);

                return ResponseEntity.ok(null);
            }

            Like like = new Like();
            like.setUsers(user);
            like.setPost(post);
            likeRepository.save(like);
           if (!Objects.equals(user.getId(), post.getUser().getId())){
               if (notificationRepository.findByRecipientAndUserFromAndNotificationTypeAndIdPost(post.getUser(),user, NotificationType.LIKE,post.getId()).isPresent()){
                   notificationRepository.deleteById(notificationRepository.findByRecipientAndUserFromAndNotificationTypeAndIdPost(post.getUser(),user,NotificationType.LIKE,post.getId()).get().getId());
               }
               Notification notification=new Notification();
               notification.setUserFrom(user);
               notification.setIsRead(false);
               notification.setRecipient(post.getUser());
               notification.setMessage(user.getFirstName()+" "+user.getLastName()+" liked your post");
               notification.setNotificationType(NotificationType.LIKE);
               notification.setIdPost(post.getId());
               notificationRepository.save(notification);
           }
            return ResponseEntity.ok(like);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @Override
    public void unlikePost(Long likeId) throws Exception {
            likeRepository.deleteById(likeId);
    }

    @Override
    public List<Like> getLikesForPost(Post post) {
        return likeRepository.findByPost(post);
    }

    @Override
    public List<Like> getLikesByUser(User user) {
        return likeRepository.findByUsers(user);
    }
}
