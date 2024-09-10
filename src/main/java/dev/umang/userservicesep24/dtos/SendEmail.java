package dev.umang.userservicesep24.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SendEmail {
    String subject;
    String from;
    String to;
    String body;
}
