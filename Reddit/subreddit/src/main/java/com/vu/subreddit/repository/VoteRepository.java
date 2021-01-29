package com.vu.subreddit.repository;

import com.vu.subreddit.model.Post;
import com.vu.subreddit.model.User;
import com.vu.subreddit.model.Vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
