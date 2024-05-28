package Mia.BackendProject.Service;

import Mia.BackendProject.Dto.CommentDto;
import Mia.BackendProject.Entity.Comment;
import Mia.BackendProject.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentInetrface {
    ResponseEntity<?> createComment(CommentDto commentDto);
    void updateComment(Long commentId, Comment updatedComment, User user) throws Exception ;
    ResponseEntity<?> deleteComment(Long commentId) throws Exception;
    List<Comment> getCommentsForPost(Long postId);
    List<Comment> getCommentsByUser(User user);
}
