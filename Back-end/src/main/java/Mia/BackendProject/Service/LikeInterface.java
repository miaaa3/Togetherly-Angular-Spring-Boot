package Mia.BackendProject.Service;

import Mia.BackendProject.Dto.LikeDto;
import Mia.BackendProject.Entity.Like;
import Mia.BackendProject.Entity.Post;
import Mia.BackendProject.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LikeInterface {
    ResponseEntity<?> likeUnlikePost(LikeDto likeDto) ;
    void unlikePost(Long likeId) throws Exception;
    List<Like> getLikesForPost(Post post);
    List<Like> getLikesByUser(User user);

}
