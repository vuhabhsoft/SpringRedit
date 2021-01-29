package com.vu.subreddit.exception;

public class PostNotFoundException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = -1671743864697024025L;

    public PostNotFoundException(String message) {
        super(message);
    }
}
