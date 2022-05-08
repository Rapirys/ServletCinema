package com.servlet.cinema.application.controller;

import com.servlet.cinema.application.entities.User;
import com.servlet.cinema.application.model.repository.UserRepository;
import com.servlet.cinema.framework.annotation.Controller;
import com.servlet.cinema.framework.annotation.GetMapping;
import com.servlet.cinema.framework.annotation.PostMapping;
import com.servlet.cinema.framework.annotation.RequestParam;
import com.servlet.cinema.framework.web.Model;
import com.servlet.cinema.framework.web.RedirectAttributes;
import org.apache.log4j.Logger;
import com.servlet.cinema.framework.security.SecurityManager;




import java.util.ArrayList;
import java.util.List;

import static com.servlet.cinema.framework.security.PasswordEncoder.encode;


@Controller
public class LoginController {
    private final static Logger logger = Logger.getLogger(RegisterController.class);


    @GetMapping(path = "/cinema/login")
    public String loginPage(@RequestParam(name = "message", required = false) String[] message,
                            Model model){
        model.addAttribute("message", message);
        return "/login.jsp";
    }

    @PostMapping(path = "/cinema/login")
    public String login(@RequestParam(name = "username") String name,
                        @RequestParam(name = "password") String password,
                        RedirectAttributes rA,
                        Model model){
        User user = new User();
        user.setUsername(name).setPassword(password);
        UserRepository userRepo = new UserRepository();
        User userFromDB = userRepo.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        userRepo.close();
        List<String> s = new ArrayList<>();
        if (userFromDB == null || !userFromDB.getPassword().equals(encode(password)))
            s.add("Invalid_username");
        if (s.size() == 0){
            System.out.println(userFromDB.getUsername()+" "+userFromDB.getAuthorities()+" "+userFromDB.getId());
            SecurityManager.addUserToSession(model, userFromDB);
            logger.debug("User id: "+userFromDB.getId()+" log in.");
            return "redirect:/cinema";
        }else {
            rA.addAttributes("message",s);
            return ("redirect:/cinema/login");
        }
    }

    @PostMapping(path = "/cinema/logout")
    public String logout(Model model){
        SecurityManager.logout(model);
        return "redirect:/cinema";
    }
}
