package com.vu.subreddit.service;

import lombok.AllArgsConstructor;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;

import com.vu.subreddit.dto.CommentsDto;
import com.vu.subreddit.exception.PostNotFoundException;
import com.vu.subreddit.mapper.CommentMapper;
import com.vu.subreddit.model.Comment;
import com.vu.subreddit.model.NotificationEmail;
import com.vu.subreddit.model.Post;
import com.vu.subreddit.model.User;
import com.vu.subreddit.repository.CommentRepository;
import com.vu.subreddit.repository.PostRepository;
import com.vu.subreddit.repository.UserRepository;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;


@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(authService.getCurrentUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .sorted(Comparator.comparing(Comment::getCreatedDate).reversed())
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .sorted(Comparator.comparing(Comment::getCreatedDate).reversed())
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}