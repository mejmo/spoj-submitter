package com.mejmo.spoj.submitter.exceptions;

/**
 * Created by MFO on 11.12.2015.
 */
public class SPOJSubmitterException extends RuntimeException {
    public SPOJSubmitterException() {
        super();
    }

    public SPOJSubmitterException(String message) {
        super(message);
    }

    public SPOJSubmitterException(String message, Throwable cause) {
        super(message, cause);
    }

    public SPOJSubmitterException(Throwable cause) {
        super(cause);
    }

    protected SPOJSubmitterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
