package com.example.demo.Post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(nullable = false)   // notNull
    private String title;       // 게시글 제목
    @Column(nullable = false, columnDefinition = "TEXT")    // notNull, 게시글 내용이 길어질 수 있어서 TEXT타입
    private String content;     // 내용
    private String author;      // UserNickname과 동일함
    private LocalDateTime createdAt;    // 게시글을 올린 시간
}