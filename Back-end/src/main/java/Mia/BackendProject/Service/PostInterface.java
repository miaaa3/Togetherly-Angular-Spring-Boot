package Mia.BackendProject.Service;

import Mia.BackendProject.Dto.PostByIdDto;
import Mia.BackendProject.Dto.PostDto;
import Mia.BackendProject.Entity.Media;
import Mia.BackendProject.Entity.Post;
import Mia.BackendProject.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostInterface {
    ResponseEntity<?> createPostWithMedia(PostDto postDto);

    Post createPostWithMedia(Post post, List<Media> mediaList);

    ResponseEntity<?> deletePost(Long postId);
    Post updatePost(Long postId, Post updatedPost, User user) throws Exception;
    List<Post> getAllPosts();
    ResponseEntity<?> postById(PostByIdDto postByIdDto);
    ResponseEntity<?> getPostAndUsersByUserAndFollowedUser(String Id);
    List<Post> getPostsByUser(User user);

}
