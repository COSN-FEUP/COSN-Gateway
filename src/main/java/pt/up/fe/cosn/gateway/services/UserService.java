package pt.up.fe.cosn.gateway.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.exceptions.NoCredentialsMatchException;
import pt.up.fe.cosn.gateway.exceptions.UserAlreadyExistsException;
import pt.up.fe.cosn.gateway.repositories.UserRepository;
import pt.up.fe.cosn.gateway.utils.Utils;

@Service  
public class UserService{

    @Autowired
    private UserRepository userRepository;

    public Boolean registerUser(User user){
        String email = user.getEmail();
        Boolean exists = userExists(email);
        if(exists)
            throw new UserAlreadyExistsException(email);

        userRepository.save(user);
        return true;
    }

    public Boolean userExists(String email){
        if(email == null)
            return false;

        return userRepository.findUserByEmail(email).isPresent();
    }

    public Optional<User> loginUser(User user) {
        Optional<User> userOptional= userRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if(userOptional.isEmpty()){
            throw new NoCredentialsMatchException();
        }
        return userOptional;
    }

    public String createJwtForUser(User user){
        return Utils.createJWT(user.getId().toString(), "GATEWAY", user.getEmail(), 28800000);
    }

    public Optional<User> getUserFromToken(String token) {
        Claims claims = Utils.decodeJWT(token);
        return userRepository.findUserByEmail(claims.getSubject());
    }
}