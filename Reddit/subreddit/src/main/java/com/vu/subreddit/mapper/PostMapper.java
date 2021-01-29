package com.vu.subreddit.mapper;

import java.time.Instant;
import java.util.Optional;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.vu.subreddit.dto.PostRequest;
import com.vu.subreddit.dto.PostResponse;
import com.vu.subreddit.model.Post;
import com.vu.subreddit.model.Subreddit;
import com.vu.subreddit.model.User;
import com.vu.subreddit.model.Vote;
import com.vu.subreddit.model.VoteType;
import com.vu.subreddit.repository.CommentRepository;
import com.vu.subreddit.repository.VoteRepository;
import com.vu.subreddit.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

    

@Component
public class PostMapper {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;
    
    public Post map (PostRequest postRequest, Subreddit subreddit, User user){
            return Post.builder()
                    .description(postRequest.getDescription())
                    .postId(postRequest.getPostId())
                    .url(postRequest.getUrl())
                    .createdDate(Instant.now())
                    .subreddit(subreddit)
                    .user(subreddit.getUser())
                    .voteCount(0)
                    .postName(postRequest.getPostName())
                    .build();
    }
    // @Mapping(target = "id", source = "postId")
    // @Mapping(target = "subredditName", source = "subreddit.name")
    // @Mapping(target = "userName", source = "user.username")
    // @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    // @Mapping(target = "duration", expression = "java(getDuration(post))")
    // @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    // @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public PostResponse mapToDto(Post post) {
        return PostResponse.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .description(post.getDescription())
                .url(post.getUrl())
                .voteCount(post.getVoteCount())
                .subredditName(post.getSubreddit().getName())
                .userName(post.getUser().getUsername())
                .commentCount(commentCount(post))
                .duration(getDuration(post))
                .upVote(isPostUpVoted(post))
                .downVote(isPostDownVoted(post))
                .build();
    }

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
    
    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, VoteType.UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, VoteType.DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

}
