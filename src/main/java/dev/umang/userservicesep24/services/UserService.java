package dev.umang.userservicesep24.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.umang.userservicesep24.dtos.SendEmail;
import dev.umang.userservicesep24.models.Token;
import dev.umang.userservicesep24.models.User;
import dev.umang.userservicesep24.repositories.TokenRepository;
import dev.umang.userservicesep24.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private KafkaTemplate<String,String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository,
                       TokenRepository tokenRepository,
                       KafkaTemplate kafkaTemplate,
                       ObjectMapper objectMapper){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public User signup(String fullName,
                       String email,
                       String password) throws JsonProcessingException {
        User user = new User();
        user.setName(fullName);
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(user);
        /*
        I want to publish this event to the message queue - kafka
         */
        SendEmail sendEmailDto = new SendEmail();
        sendEmailDto.setTo("example_receicer@gmail.com");
        //sendEmailDto.setFrom("umangonwork@gmail.com");
        sendEmailDto.setSubject("Welcome To Scaler");
        sendEmailDto.setBody("Welcome, we are very happy and excited to welcome you in the platform");


        kafkaTemplate.send(
                "send_Email",
                objectMapper.writeValueAsString(sendEmailDto)
        );

        //kafka queue - [{send_Email, {"to" : "" , from : "" , "body}]


        return user;
    }

    public Token login(String email,
                       String password){
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            //throw user not exists exception
            return null;
        }

        //user exists
        User user = optionalUser.get();
        //validate his password
        //bcrypt gives a new hash every time even for the same password

        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword())){
            //password is invalid, throw password invalid exception
            return null;
        }

        //business logic of how much time you need a token for
        //today + 30 days

        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysLater = currentDate.plusDays(30);

        Date thirtyDaysLaterdate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(thirtyDaysLaterdate);
        token.setUser(user);
        //JWT -> A.B.C https://github.com/jwtk/jjwt
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        Token savedToken = tokenRepository.save(token);
        return savedToken;
    }

    public void logout(String token){

        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedEquals(token, false);

        if(optionalToken.isEmpty()){
            //return token doesn't exist exception
            return;
        }
        Token tkn = optionalToken.get();
        tkn.setDeleted(true); //soft deleted

        tokenRepository.save(tkn);
        return;
    }

    public User validateToken(String token){
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedEqualsAndExpiryAtGreaterThan(token, false, new Date());

        if(optionalToken.isEmpty()){
            //token is invalid
            return null;
        }

        return optionalToken.get().getUser();
    }

    public User getUserDetails(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isEmpty()){
            throw new RuntimeException("User with userId + " + userId + " not present");
        }

        return optionalUser.get();
    }
}
