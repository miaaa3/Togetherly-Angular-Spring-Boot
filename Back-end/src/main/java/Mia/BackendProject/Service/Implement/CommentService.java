package Mia.BackendProject.Service.Implement;

import Mia.BackendProject.Dto.CommentDto;
import Mia.BackendProject.Entity.Comment;
import Mia.BackendProject.Entity.Notification;
import Mia.BackendProject.Entity.Post;
import Mia.BackendProject.Entity.User;
import Mia.BackendProject.Entity.enm.NotificationType;
import Mia.BackendProject.Repository.CommentRepository;
import Mia.BackendProject.Repository.NotificationRepository;
import Mia.BackendProject.Repository.PostRepository;
import Mia.BackendProject.Repository.UserRepository;
import Mia.BackendProject.Service.CommentInetrface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService implements CommentInetrface {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;


    @Autowired
    private PostRepository postRepository;

    @Override
    public ResponseEntity<?> createComment(CommentDto commentDto) {
        try {
            User user = userRepository.findById(commentDto.getIdUser())
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            Post post = postRepository.findById(commentDto.getIdPost())
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            Comment comment=new Comment();
            comment.setUsers(user);
            comment.setPost(post);
            comment.setContent(commentDto.getContent());
            commentRepository.save(comment);
            if (!Objects.equals(user.getId(), post.getUser().getId())){
                if (notificationRepository.findByRecipientAndUserFromAndNotificationTypeAndIdPost(post.getUser(),user, NotificationType.LIKE,post.getId()).isPresent()){
                    notificationRepository.deleteById(notificationRepository.findByRecipientAndUserFromAndNotificationTypeAndIdPost(post.getUser(),user,NotificationType.LIKE,post.getId()).get().getId());
                }
                Notification notification=new Notification();
                notification.setUserFrom(user);
                notification.setIsRead(false);
                notification.setRecipient(post.getUser());
                notification.setMessage(user.getFirstName()+" "+user.getLastName()+" comment your Post your post");
                notification.setNotificationType(NotificationType.COMMENT);
                notification.setIdPost(post.getId());
                notificationRepository.save(notification);
            }

            return ResponseEntity.ok("comment has been created");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @Override
    public void updateComment(Long commentId, Comment updatedComment, User user) throws Exception {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);



        existingComment.setContent(updatedComment.getContent());


        commentRepository.save(existingComment);
    }


    @Override
    public ResponseEntity<?> deleteComment(Long commentId) {
        try{
            if (commentRepository.findById(commentId).isEmpty()){
                throw new IllegalArgumentException("user does not exists !");
            }
            commentRepository.deleteById(commentId);
           return ResponseEntity.ok("comment has been deleted ");

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }




    }

    @Override
    public List<Comment> getCommentsForPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public List<Comment> getCommentsByUser(User user) {
        return commentRepository.findByUsers(user);
    }
}
