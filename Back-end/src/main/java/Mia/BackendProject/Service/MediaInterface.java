package Mia.BackendProject.Service;

import Mia.BackendProject.Entity.Media;
import Mia.BackendProject.Entity.Post;
import Mia.BackendProject.Entity.User;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaInterface {
    Media uploadMedia(MultipartFile file);
    Media getMediaById(Long mediaId) throws ChangeSetPersister.NotFoundException;
    List<Media> getAllMediaForUser(User user);
    List<Media> getAllMediaForPost(Post post);
}
