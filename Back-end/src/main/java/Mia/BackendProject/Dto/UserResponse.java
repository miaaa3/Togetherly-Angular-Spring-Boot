package Mia.BackendProject.Dto;

import Mia.BackendProject.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    String id;
    String firstName;
    String lastName;
    String userName;
    String email;
    String profileImage;
    List<Role> roles;


    public  UserResponse(String id, String firstName, String lastName, String userName, String email,String profileImage){
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.userName=userName;
        this.email=email;
        this.profileImage=profileImage;
    }
}
