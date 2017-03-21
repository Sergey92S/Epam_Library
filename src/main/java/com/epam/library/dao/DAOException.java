package com.epam.library.dao;

/**
 * Created by test on 19.03.2017.
 */
public class DAOException extends Exception {
    private static final long serialVersionUID = 1L;

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Exception e) {
        super(message, e);
    }

}