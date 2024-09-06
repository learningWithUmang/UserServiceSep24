package dev.umang.userservicesep24.dtos;

import dev.umang.userservicesep24.models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequestDTO {
    private String token;
}
