package com.vu.subreddit.exception;

public class SubredditNotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -5841933784230305658L;

    public SubredditNotFoundException(String message) {
        super(message);
    }
}
