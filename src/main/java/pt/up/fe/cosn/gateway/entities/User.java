package pt.up.fe.cosn.gateway.entities;

import lombok.Getter;
import lombok.Setter;
import pt.up.fe.cosn.gateway.utils.Utils;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;

    public User(String email, String password, Boolean alreadyEncrypted) {
        if(!alreadyEncrypted){
            password = Utils.encryptPassword(password);
        }

        this.email = email;
        this.password = password;
    }

    public User() {

    }
}

