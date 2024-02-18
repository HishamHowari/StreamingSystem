package videostreaming;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@Controller
public class LoginController {


    private final RestTemplate restTemplate;
    @Autowired
    public LoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping("/*")
    public String gateway(HttpSession session) {
        String user= (String) session.getAttribute("username");
        if(user==null)
            return "redirect:login";
        return "redirect:videostreaming";
    }

    @GetMapping("/login")
    public String getLoginPage(HttpSession session) {
        String user= (String) session.getAttribute("username");
        if(user==null)
            return "login";
        return "redirect:videostreaming";
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session){
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String checkUser(@RequestParam("username") String username, @RequestParam("password") String password, Model model,HttpSession session) {
        String url = "http://authenticationservice:8083/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User user = new User(username, password);

        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, requestEntity, Boolean.class);

        if(Boolean.TRUE.equals(response.getBody())) {
            session.setAttribute("username",username);
            return "redirect:/videostreaming";
        }

        model.addAttribute("errorMessage","Wrong username or password");
        return "login";
    }
}
