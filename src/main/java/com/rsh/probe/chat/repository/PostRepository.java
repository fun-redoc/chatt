package com.rsh.probe.chat.repository;

import com.rsh.probe.chat.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
