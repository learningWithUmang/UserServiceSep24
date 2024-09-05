package dev.umang.userservicesep24.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDTO {
    private String name;
    private String email;
    private ResponseStatus responseStatus;
}
