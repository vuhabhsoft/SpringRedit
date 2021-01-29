package com.vu.subreddit.exception;

public class SpringRedditException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -2160019770415202636L;

    public SpringRedditException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public SpringRedditException(String exMessage) {
        super(exMessage);
    }
}
