package com.example.demo.Post;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public String listPosts(Model model) {
        List<PostEntity> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "post/list"; // post/list.html
    }

    // 게시글 작성 폼
    @GetMapping("/new")
    public String showPostForm(Model model) {
        model.addAttribute("post", new PostEntity());
        return "post/new"; // post/new.html
    }

    // 게시글 저장
    @PostMapping
    public String savePost(@ModelAttribute PostEntity post) {
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
        return "redirect:/posts";
    }

    // 게시글 상세 보기
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        PostEntity post = postRepository.findById(id).orElse(null);
        model.addAttribute("post", post);
        return "post/view"; // post/view.html
    }

    // 게시글 삭제
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
        return "redirect:/posts";
    }
}