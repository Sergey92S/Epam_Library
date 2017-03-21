package com.epam.library.dao.impl;

import com.epam.library.dao.LibraryDAO;

/**
 * Created by test on 19.03.2017.
 */
public class DAOFactory {

    private static final DAOFactory INSTANCE = new DAOFactory();
    private final LibraryDAO libraryDAO = new LibraryDAOImpl();

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return INSTANCE;
    }

    public LibraryDAO getLibraryDAO() {
        return libraryDAO;
    }
}
