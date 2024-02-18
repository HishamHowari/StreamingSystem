package authenticationservice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    ArrayList<User> users;

    public AuthenticationController(){
        users = new ArrayList<>();
        users.add(new User("hisham", "1234"));
        users.add(new User("omar", "1234"));
    }
    @PostMapping("/login")
    public boolean validateUser(@RequestBody User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

}
