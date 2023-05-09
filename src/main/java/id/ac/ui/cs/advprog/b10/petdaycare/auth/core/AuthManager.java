package id.ac.ui.cs.advprog.b10.petdaycare.auth.core;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {
    private AuthManager(){}
    private static AuthManager instance;
    public static AuthManager getInstance(){
        if(instance == null){
            instance = new AuthManager();
        }
        return instance;
    }

    @Getter
    private Map<String, String> tokenToUsernameMapping = new HashMap<>();


    public void registerNewToken(String token, String username){
        // TODO
        if (tokenToUsernameMapping.containsValue(username)){
            throw new UsernameAlreadyLoggedIn();
        }
        tokenToUsernameMapping.put(token, username);
    }

    public void removeToken(String token){
        // TODO

        tokenToUsernameMapping.remove(token);
    }

    public String getUsername(String token){
        // TODO

        String username = tokenToUsernameMapping.get(token);
        if(username == null){
            throw new InvalidTokenException();
        }
        return username;
    }

}
