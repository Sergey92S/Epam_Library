package com.epam.library.pool;

import com.epam.library.dao.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by test on 19.03.2017.
 */
public class ConnectionPool {
    private static Logger logger = LogManager.getLogger(ConnectionPool.class);

    private static Lock lock = new ReentrantLock();

    private volatile static boolean instanceCreated = false;

    private static ConnectionPool instance = null;

    private static final int CONNECTION_COUNT = 5;

    private static final int MINIMAL_CONNECTION_COUNT = 5;

    private BlockingQueue<WrappedConnection> freeConnections;

    private BlockingQueue<WrappedConnection> givenConnections;

    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        if (!instanceCreated) {
            lock.lock();
            try {
                if (!instanceCreated) {
                    instance = new ConnectionPool();
                    instanceCreated = true;
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public void init() throws ConnectionPoolException {
        int connectionCount = 0;
        try {
            connectionCount = CONNECTION_COUNT;
        } catch (NumberFormatException e) {
            logger.log(Level.ERROR, "Exception while reading connection number, minimal connection count was set");
            connectionCount = MINIMAL_CONNECTION_COUNT;
        }

        freeConnections = new ArrayBlockingQueue<>(connectionCount);
        givenConnections = new ArrayBlockingQueue<>(connectionCount);

        for (int i = 0; i < connectionCount; i++) {
            try {
                WrappedConnection connection = new WrappedConnection();
                if (!connection.getAutoCommit()) {
                    connection.setAutoCommit(true);
                }
                freeConnections.put(connection);
            } catch (DAOException e) {
                throw new ConnectionPoolException("Fatal error, not obtained connection ", e);
            } catch (SQLException e) {
                throw new ConnectionPoolException("Connection SetAutoCommitException", e);
            } catch (InterruptedException e) {
                throw new ConnectionPoolException("pool error", e);
            }
        }
    }

    public WrappedConnection takeConnection() throws ConnectionPoolException {
        WrappedConnection connection;
        try {
            connection = freeConnections.take();
            givenConnections.put(connection);
        } catch (InterruptedException e) {
            throw new ConnectionPoolException("take connection error", e);
        }
        return connection;
    }

    public void returnConnection(WrappedConnection connection) throws ConnectionPoolException {
        try {
            if (connection.isNull() || connection.isClosed()) {
                throw new ConnectionPoolException("ConnectionWasLostWhileReturning Error");
            }
            try {
                if (!connection.getAutoCommit()) {
                    connection.setAutoCommit(true);
                }
                givenConnections.remove(connection);
                freeConnections.put(connection);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Connection SetAutoCommitException", e);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Interrupted exception while putting connection into freeConnectionPool", e);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "ConnectionIsClosed Error", e);
        }
    }

    public void destroyPool() {
        for (int i = 0; i < freeConnections.size(); i++) {
            try {
                WrappedConnection connection = freeConnections.take();
                connection.closeConnection();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "DestroyPoolException in freeConnections", e);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Interrupted exception while taking connection from freeConnections for close connection and destroying pool", e);
            }
        }
        for (int i = 0; i < givenConnections.size(); i++) {
            try {
                WrappedConnection connection = givenConnections.take();
                connection.closeConnection();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "DestroyPoolException in givenConnections", e);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Interrupted exception while taking connection from givenConnections for close connection and destroying pool", e);
            }
        }
        try {
            DriverManager.deregisterDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            logger.log(Level.ERROR, e + " DriverManager wasn't found");
        }
    }
}
