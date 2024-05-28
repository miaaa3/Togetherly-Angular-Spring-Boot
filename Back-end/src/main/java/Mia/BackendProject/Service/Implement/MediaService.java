package Mia.BackendProject.Service.Implement;

import Mia.BackendProject.Entity.Media;
import Mia.BackendProject.Entity.Post;
import Mia.BackendProject.Entity.User;
import Mia.BackendProject.Repository.MediaRepository;
import Mia.BackendProject.Repository.PostRepository;
import Mia.BackendProject.Repository.UserRepository;
import Mia.BackendProject.Service.MediaInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service

public class MediaService implements MediaInterface {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;


    @Override
    public Media uploadMedia(MultipartFile file) {
       /* try {
            // Check if the file is empty
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Uploaded file is empty");
            }

            // Create a new media object
            Media media = new Media();


            // Save the file to your desired storage location
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileExtension = FilenameUtils.getExtension(fileName);
            String storedFileName = UUID.randomUUID().toString() + "." + fileExtension;
            media.setMediaData(file.getBytes());

            // Set media properties
            media.setFileName(storedFileName);
            media.setFileType(fileExtension);
            media.setFileSize(file.getSize());


            // Save the media object to the database
            return media;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload media", e);
        }
*/
        return null;
    }
    @Override
    public Media getMediaById(Long mediaId) throws ChangeSetPersister.NotFoundException {
        return mediaRepository.findById(mediaId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    @Override
    public List<Media> getAllMediaForUser(User user) {
        return mediaRepository.findByUser(user);
    }

    @Override
    public List<Media> getAllMediaForPost(Post post) {
        return mediaRepository.findByPost(post);
    }
}
