package com.vu.subreddit.mapper;

import java.time.Instant;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.vu.subreddit.dto.CommentsDto;
import com.vu.subreddit.model.Comment;
import com.vu.subreddit.model.Post;
import com.vu.subreddit.model.User;

import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    
    // @Mapping(target = "id", ignore = true)
    // @Mapping(target = "text", source = "commentsDto.text")
    // @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    // @Mapping(target = "post", source = "post")
    // @Mapping(target = "user", source = "user")
    // Comment map(CommentsDto commentsDto, Post post, User user);

    public Comment map (CommentsDto commentsDto, Post post, User user){
        return Comment.builder()
                .id(commentsDto.getPostId())
                .text(commentsDto.getText())
                .createdDate(Instant.now())
                .post(post)
                .user(user)
                .build();
    }

    // @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    // @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    // CommentsDto mapToDto(Comment comment);private Long id;
    // private Long postId;
    // private Instant createdDate;
    // private String text;
    // private String userName;
    public CommentsDto mapToDto(Comment comment){
        return CommentsDto.builder()
              .id(comment.getId())
              .duration(getDuration(comment))
              .text(comment.getText())
              .postId(comment.getPost().getPostId())
              .userName(comment.getUser().getUsername())
              .build();
    }
    
    String getDuration(Comment comment) {
        return TimeAgo.using(comment.getCreatedDate().toEpochMilli());
    }
}
