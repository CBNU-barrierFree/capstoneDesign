package com.example.demo.Comment;

import com.example.demo.User.UserEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 추가
    @PostMapping("/add")
    public String addComment(@RequestParam("postId") Long postId,
                             @RequestParam("content") String content,
                             HttpSession session) {

        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }

        commentService.addComment(postId, user.getId(), content);
        return "redirect:/posts/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable("commentId") Long commentId, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }

        Long postId = commentService.deleteCommentIfOwner(commentId, user.getId());
        return "redirect:/posts/" + postId;
    }
}