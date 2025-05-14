package com.example.demo.Post;

import com.example.demo.Comment.CommentEntity;
import com.example.demo.Comment.CommentService;
import com.example.demo.User.CustomUserDetails; // 사용자 인증 정보에서 nickname 추출하려면 필요
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentService commentService;

    @GetMapping
    public String listPosts(Model model) {
        List<PostEntity> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "post/list";
    }

    // 게시글 작성 폼
    @GetMapping("/new")
    public String showPostForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // 로그인 안 됐으면 로그인 페이지로
        }
        model.addAttribute("post", new PostEntity());
        return "post/new";
    }

    // 게시글 저장
    @PostMapping
    public String savePost(@ModelAttribute PostEntity post, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // 비정상 접근 방지
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        post.setAuthor(userDetails.getNickname()); // 자동으로 닉네임 설정
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        PostEntity post = postRepository.findById(id).orElseThrow();
        List<CommentEntity> comments = commentService.getCommentsByPost(id);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "post/view";
    }


    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        PostEntity post = postRepository.findById(id).orElse(null);

        if (post == null) {
            return "redirect:/posts";
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String currentNickname = userDetails.getNickname();

        if (!post.getAuthor().equals(currentNickname)) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 게시물을 삭제할 권한이 없습니다.");
            return "redirect:/posts";
        }

        postRepository.deleteById(id);
        return "redirect:/posts";
    }
}