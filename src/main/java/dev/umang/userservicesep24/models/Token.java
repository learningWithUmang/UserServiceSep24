package dev.umang.userservicesep24.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Token extends BaseModel{
    private String value;

    //Token : User => 1 1
                    //M 1
    @ManyToOne
    private User user;

    private Date expiryAt;
}
