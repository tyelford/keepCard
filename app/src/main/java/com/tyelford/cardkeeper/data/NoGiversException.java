package com.tyelford.cardkeeper.data;

/**
 * Created by localadmin on 21/05/15.
 */
public class NoGiversException extends Exception {

    public NoGiversException(){super();}

    public NoGiversException(String message){super(message);}

    public NoGiversException(String message, Throwable cause){ super(message, cause); }

    public NoGiversException(Throwable cause){ super(cause); }

}
