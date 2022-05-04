package com.servlet.cinema.application.controller;

import com.servlet.cinema.application.entities.Role;
import com.servlet.cinema.application.entities.User;
import com.servlet.cinema.application.model.repository.UserRepository;
import com.servlet.cinema.application.model.service.Validator;
import com.servlet.cinema.framework.annotation.Controller;

import com.servlet.cinema.framework.annotation.GetMapping;
import com.servlet.cinema.framework.annotation.PostMapping;
import com.servlet.cinema.framework.annotation.RequestParam;
import com.servlet.cinema.framework.data.ConnectionPool;
import com.servlet.cinema.framework.security.PasswordEncoder;
import com.servlet.cinema.framework.web.Model;
import com.servlet.cinema.framework.web.RedirectAttributes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
public class RegisterController {
    private final static Logger logger = Logger.getLogger(RegisterController.class);


    @GetMapping(path = "/cinema/register")
    public String register (@RequestParam(name = "message", required = false) String[] message,
                            Model model){
        model.addAttribute("message", message);
        return "register.jsp";
    }

    @PostMapping(path = "/cinema/register")
    public String addUser (@RequestParam(name = "username") String name,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "password") String password, RedirectAttributes redirectAttributes){
        User user = new User(name, email, password);
        List<String> s= Validator.validUserFields(user);
        if (s.size()!=0) {
            redirectAttributes.addAttributes("message",s);
            return "redirect:register";
        }
        Set<Role> roles= new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles).setPassword(PasswordEncoder.encode(user.getPassword()));
        UserRepository userRepository= new UserRepository();
        try {
            userRepository.save(user);
        } catch (Exception e){
            s.add("User_already_exist");
            redirectAttributes.addAttributes("message",s);
            userRepository.close();
            return "redirect:register";
        }
        userRepository.close();
        logger.debug("User name: "+user.getUsername()+" registered.");
        return "redirect:login";
    }

}
