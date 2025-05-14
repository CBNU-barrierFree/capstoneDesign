package com.example.demo.Comment;

import com.example.demo.Post.PostEntity;
import com.example.demo.Post.PostRepository;
import com.example.demo.User.UserEntity;
import com.example.demo.User.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void addComment(Long postId, Long userId, String content) {
        PostEntity post = postRepository.findById(postId).orElseThrow();
        UserEntity user = userRepository.findById(userId).orElseThrow();

        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
    }

    public List<CommentEntity> getCommentsByPost(Long postId) {
        PostEntity post = postRepository.findById(postId).orElseThrow();
        return commentRepository.findByPost(post);
    }

    public Long deleteCommentIfOwner(Long commentId, Long userId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow();

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        Long postId = comment.getPost().getId(); // 삭제 후 돌아갈 게시글 id
        commentRepository.delete(comment);
        return postId;
    }
}