package com.epam.library.service.impl;

import com.epam.library.dao.DAOException;
import com.epam.library.dao.LibraryDAO;
import com.epam.library.dao.impl.DAOFactory;
import com.epam.library.domain.Book;
import com.epam.library.domain.Employee;
import com.epam.library.domain.EmployeeBook;
import com.epam.library.service.LibraryService;
import com.epam.library.service.ServiceException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * Created by test on 19.03.2017.
 */
public class LibraryServiceImpl implements LibraryService {
    private static Random rand = new Random();
    private static final int MAX_ORDER_ID = 1000000;


    @Override
    public boolean createNewBook(Book book) {
        return false;
    }

    @Override
    public boolean deleteBookById(int id) {
        return false;
    }

    @Override
    public boolean generateEmployeeBookList() throws ServiceException {
        LibraryDAO libraryDAO = DAOFactory.getInstance().getLibraryDAO();
        boolean generated = false;
        try {
            ArrayList<Integer> employeeIdList = libraryDAO.takeEmployeeIdList();
            ArrayList<Integer> bookIdList = libraryDAO.takeBookIdList();
            ArrayList<EmployeeBook> employeeBooks = generateEmplBooks(employeeIdList, bookIdList);
            generated = libraryDAO.addEmployeeBookList(employeeBooks);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return generated;
    }


    @Override
    public boolean renameBookById(String oldTitle, String newTitle) throws ServiceException {
        boolean renamed = false;
        LibraryDAO libraryDAO = DAOFactory.getInstance().getLibraryDAO();

        try{
            if(oldTitle.endsWith("*")){
                renamed = libraryDAO.renameBookWithMask(oldTitle.substring(0, oldTitle.length()-1), newTitle);
            } else {
                renamed = libraryDAO.renameBook(oldTitle, newTitle);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return renamed;
    }

    @Override
    public Book showBookById(int id) {
        return null;
    }

    @Override
    public LinkedHashMap<String, Integer> reportAboutGoodReaders() throws ServiceException {
        LinkedHashMap<String, Integer> readers;
        LibraryDAO libraryDAO = DAOFactory.getInstance().getLibraryDAO();

        try {
            readers = libraryDAO.findGoodReaders();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return readers;
    }

    @Override
    public LinkedHashMap<Employee, Integer> reportAboutLittleReaders() throws ServiceException {
        LinkedHashMap<Employee, Integer> readers;
        LibraryDAO libraryDAO = DAOFactory.getInstance().getLibraryDAO();
        try {
            readers = libraryDAO.findNotSoGoodReaders();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return readers;
    }


    private ArrayList<EmployeeBook> generateEmplBooks(ArrayList<Integer> employeeIdList, ArrayList<Integer> bookIdList) {
        ArrayList<EmployeeBook> resultList = new ArrayList<>();
        EmployeeBook employeeBook = null;
        int bookNumber;
        int noBookEmployeeNumber = rand.nextInt(employeeIdList.size());
        for (int j = 0; j < employeeIdList.size(); j++) {
            if (j == noBookEmployeeNumber) {
                continue;
            }
            int count = rand.nextInt(bookIdList.size());
            int currentCount = count;
            for (int i = 0; i < count; i++) {
                employeeBook = new EmployeeBook();
                employeeBook.setEmployeeId(employeeIdList.get(j));
                if (currentCount == bookIdList.size()) {
                    currentCount = 0;
                }
                bookNumber = bookIdList.get(currentCount);
                currentCount++;
                employeeBook.setBookId(bookNumber);
                employeeBook.setId(rand.nextInt(MAX_ORDER_ID));
                resultList.add(employeeBook);
            }
        }
        return resultList;
    }
}