package com.rsh.probe.chat.service;

import com.rsh.probe.chat.model.Post;
import com.rsh.probe.chat.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private  PostRepository postRepository;

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public void save(Post post) {
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

}
