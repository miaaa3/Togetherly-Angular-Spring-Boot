package Mia.BackendProject.Service.Implement;

import Mia.BackendProject.Dto.FollowDto;
import Mia.BackendProject.Entity.FollowRelation;
import Mia.BackendProject.Entity.Notification;
import Mia.BackendProject.Entity.User;
import Mia.BackendProject.Entity.enm.NotificationType;
import Mia.BackendProject.Repository.FollowRelationRepository;
import Mia.BackendProject.Repository.NotificationRepository;
import Mia.BackendProject.Repository.UserRepository;
import Mia.BackendProject.Service.FollowInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class FollowService implements FollowInterface {
    @Autowired
    private FollowRelationRepository followRelationshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<?> notFollowed(String id){
        return ResponseEntity.ok(followRelationshipRepository.findUsersNotFollowedBy(id));
    }
    @Override
    public ResponseEntity<?> findUserFriend(String id){
        return ResponseEntity.ok(followRelationshipRepository.findUserFriend(id));
    }
    @Override
    public ResponseEntity<?> follow(FollowDto followDto) {
        try{
            if (!userRepository.findById(followDto.getFollowed()).isPresent()){
                throw new IllegalArgumentException("follower does not exists !");
            }
            if (!userRepository.findById(followDto.getFollowing()).isPresent()){
                throw new IllegalArgumentException("followed does not exists !");
            }
            User follower = userRepository.findById(followDto.getFollowing()).get();
            User followed = userRepository.findById(followDto.getFollowed()).get();
            Optional<FollowRelation> follow=followRelationshipRepository.findByFollowerAndFollowed(follower,followed);
             if (!follow.isPresent()) {
                 FollowRelation relationship = new FollowRelation();
                 relationship.setFollower(follower);
                 relationship.setFollowed(followed);
                 followRelationshipRepository.save(relationship);
                 if (!Objects.equals(followed.getId(), follower.getId())){
                     if (notificationRepository.findByRecipientAndUserFromAndNotificationType(followed,follower, NotificationType.FOLLOW).isPresent()){
                         notificationRepository.deleteById(notificationRepository.findByRecipientAndUserFromAndNotificationType(followed,follower,NotificationType.FOLLOW).get().getId());
                     }
                     Notification notification=new Notification();
                     notification.setUserFrom(follower);
                     notification.setIsRead(false);
                     notification.setRecipient(followed);
                     notification.setMessage(" you are followed by "+follower.getFirstName()+" "+follower.getLastName());
                     notification.setNotificationType(NotificationType.FOLLOW);
                     notification.setEmailFrom(follower.getEmail());
                     notificationRepository.save(notification);
                 }
                 return ResponseEntity.ok("has been followed");
             }
                 followRelationshipRepository.deleteById(follow.get().getId());
            return ResponseEntity.ok("has been unfollowed");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> checkIfFriend(String idUser1,String idUser2){
        try {
            if (!userRepository.findById(idUser1).isPresent()){
                throw new IllegalArgumentException("follower does not exists !");
            }
            if (!userRepository.findById(idUser2).isPresent()){
                throw new IllegalArgumentException("followed does not exists !");
            }
            User follower = userRepository.findById(idUser1).get();
            User followed = userRepository.findById(idUser2).get();

            if (followRelationshipRepository.findByFollowerAndFollowed(follower,followed).isPresent() && followRelationshipRepository.findByFollowerAndFollowed(followed,follower).isPresent()){
                return ResponseEntity.ok(true);
            }
                return  ResponseEntity.ok(false);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> checkIfFollow(FollowDto followDto){
        try{
            if (!userRepository.findById(followDto.getFollowed()).isPresent()){
                throw new IllegalArgumentException("follower does not exists !");
            }
            if (!userRepository.findById(followDto.getFollowing()).isPresent()){
                throw new IllegalArgumentException("followed does not exists !");
            }
            User follower = userRepository.findById(followDto.getFollowing()).get();
            User followed = userRepository.findById(followDto.getFollowed()).get();
            Optional<FollowRelation> follow=followRelationshipRepository.findByFollowerAndFollowed(follower,followed);
            if (follow.isPresent()){
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.ok(false);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
