package com.epam.library.dao.impl;

import com.epam.library.dao.DAOException;
import com.epam.library.dao.LibraryDAO;
import com.epam.library.domain.Employee;
import com.epam.library.domain.EmployeeBook;
import com.epam.library.pool.ConnectionPool;
import com.epam.library.pool.ConnectionPoolException;
import com.epam.library.pool.WrappedConnection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by test on 19.03.2017.
 */
public class LibraryDAOImpl implements LibraryDAO {
    private static Logger logger = LogManager.getLogger(LibraryDAOImpl.class);


    private static final String SELECT_EMPLOYEES_ID = "SELECT id FROM epam_library.employee;";

    private static final String SELECT_BOOKS_ID = "SELECT id FROM epam_library.book;";

    private static final String INSERT_EMPLOYEE_BOOK = "INSERT INTO `epam_library`.`employee_book` (`employee_id`, `book_id`, `id`) VALUES (?,?,?);";

    private static final String SELECT_READER_WHO_READ_LOT = "SELECT employee.name, coalesce(COUNT(employee_book.employee_id),0) AS book_number\n" +
            "FROM epam_library.employee_book \n" +
            "JOIN epam_library.employee ON employee.id=employee_book.employee_id\n" +
            "GROUP BY employee_id HAVING book_number>1 ORDER BY book_number;";

    private static final String SELECT_READERS_WHO_DONT_READ_LOT = "SELECT employee.name, employee.date_of_birth, coalesce(count(employee_book.employee_id), 0) AS book_number\n" +
            "FROM epam_library.employee_book \n" +
            "RIGHT JOIN epam_library.employee ON employee.id=employee_book.employee_id\n" +
            "GROUP BY employee_id HAVING book_number<=2 ORDER BY date_of_birth, book_number DESC;";

    private static final String SELECT_IDS_BY_TITLE = "SELECT id FROM epam_library.book WHERE title=?";

    private static final String UPDATE_TITLE = "UPDATE epam_library.book SET title=? WHERE id = ?;";

    private static final String SELECT_IDS_BY_TITLE_WITH_MASK = "SELECT id FROM epam_library.book WHERE title LIKE ?";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BOOK_NUMBER = "book_number";
    private static final String DATE_OF_BIRTH = "date_of_birth";

    @Override
    public ArrayList<Integer> takeEmployeeIdList() throws DAOException {
        WrappedConnection connection = null;
        ArrayList<Integer> employees = new ArrayList<>();

        Statement selectEmployeesIdStatement = null;
        ResultSet idListResultSet;

        try {
            connection = ConnectionPool.getInstance().takeConnection();
            selectEmployeesIdStatement = connection.getStatement();
            idListResultSet = selectEmployeesIdStatement.executeQuery(SELECT_EMPLOYEES_ID);
            if (idListResultSet.next()) {
                idListResultSet.beforeFirst();
                employees = new ArrayList<>();
                while (idListResultSet.next()) {
                    employees.add(idListResultSet.getInt(ID));
                }
            }
        } catch (ConnectionPoolException e) {
            throw new DAOException("Error while taking connection from ConnectionPool", e);
        } catch (SQLException e) {
            throw new DAOException("SQL Error, check source", e);
        } finally {
            connection.closeStatement(selectEmployeesIdStatement);
            try {
                ConnectionPool.getInstance().returnConnection(connection);
            } catch (ConnectionPoolException e) {
                logger.log(Level.ERROR, "Error while returning connection to ConnectionPool", e);
            }
        }
        return employees;
    }

    @Override
    public ArrayList<Integer> takeBookIdList() throws DAOException {
        ArrayList<Integer> books = new ArrayList<>();
        WrappedConnection connection = null;

        Statement selectBooksIdStatement = null;
        ResultSet idListResultSet;

        try {
            connection = ConnectionPool.getInstance().takeConnection();
            selectBooksIdStatement = connection.getStatement();
            idListResultSet = selectBooksIdStatement.executeQuery(SELECT_BOOKS_ID);
            if (idListResultSet.next()) {
                books = new ArrayList<>();
                idListResultSet.beforeFirst();
                while (idListResultSet.next()) {
                    books.add(idListResultSet.getInt(ID));
                }
            }

        } catch (ConnectionPoolException e) {
            throw new DAOException("Error while taking connection from ConnectionPool", e);
        } catch (SQLException e) {
            throw new DAOException("SQL Error, check source", e);
        } finally {
            connection.closeStatement(selectBooksIdStatement);
            try {
                ConnectionPool.getInstance().returnConnection(connection);
            } catch (ConnectionPoolException e) {
                logger.log(Level.ERROR, "Error while returning connection to ConnectionPool", e);
            }
        }
        return books;
    }

    @Override
    public boolean addEmployeeBookList(ArrayList<EmployeeBook> employeeBooks) throws DAOException {
        boolean added = false;
        WrappedConnection connection = null;
        PreparedStatement addEBListStatement = null;
        try {
            connection = ConnectionPool.getInstance().takeConnection();
            connection.setAutoCommit(false);
            addEBListStatement = connection.prepareStatement(INSERT_EMPLOYEE_BOOK);
            for (int i = 0; i < employeeBooks.size(); i++) {
                addEBListStatement.setInt(1, employeeBooks.get(i).getEmployeeId());
                addEBListStatement.setInt(2, employeeBooks.get(i).getBookId());
                addEBListStatement.setInt(3, employeeBooks.get(i).getId());
                addEBListStatement.addBatch();
            }
            addEBListStatement.executeBatch();
            added = true;
        } catch (ConnectionPoolException e) {
            throw new DAOException("Error while taking connection from ConnectionPool", e);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.log(Level.ERROR, "Exception while connection rollback, " + e1);
            }
            throw new DAOException("SQL Error, check source", e);
        } finally {
            connection.closeStatement(addEBListStatement);
            try {
                ConnectionPool.getInstance().returnConnection(connection);
            } catch (ConnectionPoolException e) {
                logger.log(Level.ERROR, "Error while returning connection to ConnectionPool", e);
            }
        }

        return added;
    }

    @Override
    public LinkedHashMap<String, Integer> findGoodReaders() throws DAOException {
        LinkedHashMap<String, Integer> readers = null;
        WrappedConnection connection = null;

        Statement selectReadersStatement = null;
        ResultSet readersListResultSet;

        try {
            connection = ConnectionPool.getInstance().takeConnection();
            selectReadersStatement = connection.getStatement();
            readersListResultSet = selectReadersStatement.executeQuery(SELECT_READER_WHO_READ_LOT);
            if (readersListResultSet.next()) {
                readers = new LinkedHashMap<>();
                readersListResultSet.beforeFirst();
                while (readersListResultSet.next()) {
                    String name = readersListResultSet.getString(NAME);
                    int bookNumber = readersListResultSet.getInt(BOOK_NUMBER);
                    readers.put(name, bookNumber);
                }
            }
        } catch (ConnectionPoolException e) {
            throw new DAOException("Error while taking connection from ConnectionPool", e);
        } catch (SQLException e) {
            throw new DAOException("SQL Error, check source", e);
        } finally {
            connection.closeStatement(selectReadersStatement);
            try {
                ConnectionPool.getInstance().returnConnection(connection);
            } catch (ConnectionPoolException e) {
                logger.log(Level.ERROR, "Error while returning connection to ConnectionPool", e);
            }
        }
        return readers;
    }

    @Override
    public LinkedHashMap<Employee, Integer> findNotSoGoodReaders() throws DAOException {
        LinkedHashMap<Employee, Integer> readers = null;
        WrappedConnection connection = null;
        Employee employee;

        Statement selectReadersStatement = null;
        ResultSet readersListResultSet;

        try {
            connection = ConnectionPool.getInstance().takeConnection();
            selectReadersStatement = connection.getStatement();
            readersListResultSet = selectReadersStatement.executeQuery(SELECT_READERS_WHO_DONT_READ_LOT);
            if (readersListResultSet.next()) {
                readers = new LinkedHashMap<>();
                readersListResultSet.beforeFirst();
                while (readersListResultSet.next()) {
                    employee = new Employee();
                    String name = readersListResultSet.getString(NAME);
                    Date dateOfBirth = readersListResultSet.getDate(DATE_OF_BIRTH);
                    employee.setName(name);
                    employee.setDateOfBirth(dateOfBirth);
                    int bookNumber = readersListResultSet.getInt(BOOK_NUMBER);
                    readers.put(employee, bookNumber);
                }
            }
        } catch (ConnectionPoolException e) {
            throw new DAOException("Error while taking connection from ConnectionPool", e);
        } catch (SQLException e) {
            throw new DAOException("SQL Error, check source", e);
        } finally {
            connection.closeStatement(selectReadersStatement);
            try {
                ConnectionPool.getInstance().returnConnection(connection);
            } catch (ConnectionPoolException e) {
                logger.log(Level.ERROR, "Error while returning connection to ConnectionPool", e);
            }
        }
        return readers;
    }

    @Override
    public boolean renameBook(String oldTitle, String newTitle) throws DAOException {

        boolean renamed = false;
        WrappedConnection connection = null;
        ArrayList<Integer> bookIdList = new ArrayList<>();

        PreparedStatement selectAllIdByTitleStatement = null;
        ResultSet allIdByTitleResultSet;
        PreparedStatement updateTitleStatement = null;
        try {
            connection = ConnectionPool.getInstance().takeConnection();
            connection.setAutoCommit(false);
            selectAllIdByTitleStatement = connection.prepareStatement(SELECT_IDS_BY_TITLE);
            selectAllIdByTitleStatement.setString(1, oldTitle);
            allIdByTitleResultSet = selectAllIdByTitleStatement.executeQuery();
            if (allIdByTitleResultSet.next()) {
                allIdByTitleResultSet.beforeFirst();
                while (allIdByTitleResultSet.next()) {
                    int id = allIdByTitleResultSet.getInt(ID);
                    bookIdList.add(id);
                }
            }
            updateTitleStatement = connection.prepareStatement(UPDATE_TITLE);
            for (int i = 0; i < bookIdList.size(); i++) {
                updateTitleStatement.setString(1, newTitle);
                updateTitleStatement.setInt(2, bookIdList.get(i));
                updateTitleStatement.addBatch();
            }
            updateTitleStatement.executeBatch();
            renamed = true;
        } catch (ConnectionPoolException e) {
            throw new DAOException("Error while taking connection from ConnectionPool", e);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.log(Level.ERROR, "Exception while connection rollback, " + e1);
            }
            throw new DAOException("SQL Error, check source", e);
        } finally {
            connection.closeStatement(selectAllIdByTitleStatement);
            connection.closeStatement(updateTitleStatement);
            try {
                ConnectionPool.getInstance().returnConnection(connection);
            } catch (ConnectionPoolException e) {
                logger.log(Level.ERROR, "Error while returning connection to ConnectionPool", e);
            }
        }

        return renamed;
    }

    @Override
    public boolean renameBookWithMask(String oldTitle, String newTitle) throws DAOException {
        boolean renamed = false;
        WrappedConnection connection = null;
        ArrayList<Integer> bookIdList = new ArrayList<>();

        PreparedStatement selectAllIdByTitleStatement = null;
        ResultSet allIdByTitleResultSet;
        PreparedStatement updateTitleStatement = null;
        try {
            connection = ConnectionPool.getInstance().takeConnection();
            connection.setAutoCommit(false);
            selectAllIdByTitleStatement = connection.prepareStatement(SELECT_IDS_BY_TITLE_WITH_MASK);
            selectAllIdByTitleStatement.setString(1, oldTitle);
            allIdByTitleResultSet = selectAllIdByTitleStatement.executeQuery();
            if (allIdByTitleResultSet.next()) {
                allIdByTitleResultSet.beforeFirst();
                while (allIdByTitleResultSet.next()) {
                    int id = allIdByTitleResultSet.getInt(ID);
                    bookIdList.add(id);
                }
            }
            updateTitleStatement = connection.prepareStatement(UPDATE_TITLE);
            for (int i = 0; i < bookIdList.size(); i++) {
                updateTitleStatement.setString(1, newTitle);
                updateTitleStatement.setInt(2, bookIdList.get(i));
                updateTitleStatement.addBatch();
            }
            updateTitleStatement.executeBatch();
            renamed = true;
        } catch (ConnectionPoolException e) {
            throw new DAOException("Error while taking connection from ConnectionPool", e);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.log(Level.ERROR, "Exception while connection rollback, " + e1);
            }
            throw new DAOException("SQL Error, check source", e);
        } finally {
            connection.closeStatement(selectAllIdByTitleStatement);
            connection.closeStatement(updateTitleStatement);
            try {
                ConnectionPool.getInstance().returnConnection(connection);
            } catch (ConnectionPoolException e) {
                logger.log(Level.ERROR, "Error while returning connection to ConnectionPool", e);
            }
        }

        return renamed;
    }
}
