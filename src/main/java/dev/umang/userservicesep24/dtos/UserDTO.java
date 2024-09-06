package dev.umang.userservicesep24.dtos;

import dev.umang.userservicesep24.models.Role;
import dev.umang.userservicesep24.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String name;
    private String email;
    private List<Role> roles;
    private boolean isEmailVerified;

    public static UserDTO from(User user){
        if(user == null) return null;

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setRoles(user.getRoles());
        userDTO.setEmailVerified(user.isEmailVerified());

        return  userDTO;
    }

}
