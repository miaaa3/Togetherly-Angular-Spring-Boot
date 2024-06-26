package Mia.BackendProject.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NumberOfLikesFollowersFollowingDto {
    long numberOfLikes;
    long numberOfFollowers;
    long numberOfFollowing;
}
