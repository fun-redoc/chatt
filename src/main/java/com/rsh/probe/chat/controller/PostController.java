package com.rsh.probe.chat.controller;

import com.rsh.probe.chat.model.Post;
import com.rsh.probe.chat.service.PostService;
import com.rsh.probe.chat.service.UserService;
import com.rsh.probe.chat.websocket.ChatWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.socket.WebSocketHandler;

import java.io.IOException;

@Controller
public class PostController {
    @Autowired
    private UserService userService;
    @Autowired
    PostService postService;

    @Autowired
    WebSocketHandler chatWebSocketHandler;

    @GetMapping("/")
    public String index(Model model) {
        var posts = postService.getAll();
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/newPost")
    public String newPost(Model model) {
        model.addAttribute("newPost", new Post());
        return "new-post";
    }

    @PostMapping("/newPost")
    public String savePost(@ModelAttribute(name = "newPost") Post newPost) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = auth.getPrincipal();
        var userName = ((UserDetails)userDetails).getUsername(); // ugly upcast, so is the framework
        var maybeRegisteredUser = userService.byUsername(userName);
        return maybeRegisteredUser
                .map(u -> {
                    newPost.setUser(u);
                    postService.save(newPost);
                    try {
                        ((ChatWebSocketHandler)chatWebSocketHandler).broadcast(newPost.toString()); // TODO find a way to overcome this ugly upcast
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return "redirect:/";
                })
                .orElseThrow(() -> new UsernameNotFoundException("User is not registered, please register and try again later."));
            }

}
