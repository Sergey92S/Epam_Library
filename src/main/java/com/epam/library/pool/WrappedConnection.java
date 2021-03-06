package com.epam.library.pool;

import com.epam.library.dao.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Created by test on 19.03.2017.
 */
public class WrappedConnection {
    private static Logger logger = LogManager.getLogger(WrappedConnection.class);

    static {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            logger.log(Level.FATAL, e + " DriverManager wasn't found");
            throw new RuntimeException(e);
        }
    }

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1/CorporateBooksReadingCounter";

    private static final String DB_LOGIN = "root";

    private static final String DB_PASSWORD = "root";

    private static final String DB_PROPERTIES_FILE = "resources.db";

    private Connection connection;

    WrappedConnection() throws DAOException {
        try {
            connection = createConnection();
        } catch (SQLException e) {
            throw new DAOException("Not obtained connection ", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private static Connection createConnection() throws SQLException {
        String url = JDBC_URL;
        String login = DB_LOGIN;
        String password = DB_PASSWORD;

        return DriverManager.getConnection(url, login, password);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    public Statement getStatement() throws SQLException {
        if (connection != null) {
            Statement statement = connection.createStatement();
            if (statement != null) {
                return statement;
            }
        }
        throw new SQLException("connection or statement is null");
    }

    public void closeStatement(Statement statement) throws DAOException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Statement closing exception ", e);
            }
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    public boolean isNull() {
        return connection == null;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

}
