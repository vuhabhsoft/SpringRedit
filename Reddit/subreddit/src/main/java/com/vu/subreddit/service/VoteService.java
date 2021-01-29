package com.vu.subreddit.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.vu.subreddit.dto.VoteDto;
import com.vu.subreddit.exception.PostNotFoundException;
import com.vu.subreddit.model.Post;
import com.vu.subreddit.model.Vote;
import com.vu.subreddit.model.VoteType;
import com.vu.subreddit.repository.PostRepository;
import com.vu.subreddit.repository.VoteRepository;
import static com.vu.subreddit.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            if (UPVOTE.equals(voteDto.getVoteType())) {
                post.setVoteCount(post.getVoteCount() - 1);              
            } 
            else
            {
                post.setVoteCount(post.getVoteCount() + 1);
            }
            voteDto.setVoteType(VoteType.NEUTRAL);
        }
        
        else if(voteByPostAndUser.isPresent() &&
        voteByPostAndUser.get().getVoteType()
                .equals(VoteType.DOWNVOTE) && voteDto.getVoteType().equals(VoteType.UPVOTE)){
                    post.setVoteCount(post.getVoteCount() + 2);
        }

        else if(voteByPostAndUser.isPresent() &&
        voteByPostAndUser.get().getVoteType()
                .equals(VoteType.UPVOTE) && voteDto.getVoteType().equals(VoteType.DOWNVOTE)){
                    post.setVoteCount(post.getVoteCount() - 2);
        }
         
        else{
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}