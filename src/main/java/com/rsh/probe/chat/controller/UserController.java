package com.rsh.probe.chat.controller;

import com.rsh.probe.chat.model.User;
import com.rsh.probe.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        // see security/SecurityConfiguration.java the build up of the security filter chain for login
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        // see security/SecurityConfiguration.java the build up of the security filter chain for register
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String saveRegisteredUser(Model model, @ModelAttribute User user) {
       var maybeUser = userService.byUsername(user.getUsername());
       if( maybeUser.isEmpty()) {
           userService.save(user);
           return "redirect:/register?success";
       } else {
           return "redirect:/register?fail";
       }

    }

}
