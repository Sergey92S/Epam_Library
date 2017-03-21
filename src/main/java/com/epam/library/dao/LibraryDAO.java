package com.epam.library.dao;

import com.epam.library.domain.Employee;
import com.epam.library.domain.EmployeeBook;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by test on 19.03.2017.
 */
public interface LibraryDAO {


    ArrayList<Integer> takeEmployeeIdList() throws DAOException;

    ArrayList<Integer> takeBookIdList() throws DAOException;

    boolean addEmployeeBookList(ArrayList<EmployeeBook> employeeBooks) throws DAOException;

    LinkedHashMap<String, Integer> findGoodReaders() throws DAOException;

    LinkedHashMap<Employee, Integer> findNotSoGoodReaders() throws DAOException;

    boolean renameBook(String oldTitle, String newTitle) throws DAOException;

    boolean renameBookWithMask(String oldTitle, String newTitle) throws DAOException;
}