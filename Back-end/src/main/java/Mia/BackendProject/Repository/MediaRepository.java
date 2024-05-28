package Mia.BackendProject.Repository;

import Mia.BackendProject.Entity.Media;
import Mia.BackendProject.Entity.Post;
import Mia.BackendProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MediaRepository extends JpaRepository<Media,Long> {
    List<Media> findByUser(User user);

    List<Media> findByPost(Post post);
}
