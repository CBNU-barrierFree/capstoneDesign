package com.example.demo.Post;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    public void savePost(PostEntity post) {
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    public PostEntity getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }
}

