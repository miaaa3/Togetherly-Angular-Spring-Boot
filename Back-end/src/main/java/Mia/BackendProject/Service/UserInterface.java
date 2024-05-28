package Mia.BackendProject.Service;


import Mia.BackendProject.Dto.UserReqDto;
import Mia.BackendProject.Entity.Role;
import Mia.BackendProject.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserInterface {
    User saveUser(User user);

    Optional<User> getUserById(String id);

    ResponseEntity<?> updateProfile(UserReqDto userReqDto);



    ResponseEntity<?> getUserByEmail(String email);

    Role saveRole(Role role);
    void addToUser(String username,String roleName);

    ResponseEntity<?> getNumberOfLikesFollowersFollowing(String id);

    ResponseEntity<?> findAllUsers();


}
