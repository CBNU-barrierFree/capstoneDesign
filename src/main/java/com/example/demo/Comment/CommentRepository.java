package com.example.demo.Comment;

import com.example.demo.Post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository <CommentEntity, Long>{
    List<CommentEntity> findByPost(PostEntity post);
}