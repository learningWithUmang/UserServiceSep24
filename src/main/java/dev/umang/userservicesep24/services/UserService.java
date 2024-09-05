package dev.umang.userservicesep24.services;

import dev.umang.userservicesep24.models.User;
import dev.umang.userservicesep24.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    public User signup(String fullName,
                       String email,
                       String password){
        User user = new User();
        user.setName(fullName);
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(user);
        return user;
    }
}