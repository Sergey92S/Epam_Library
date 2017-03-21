package com.epam.library.main;

import com.epam.library.command.CommandName;
import com.epam.library.controller.Controller;
import com.epam.library.domain.Employee;
import com.epam.library.domain.Request;
import com.epam.library.domain.Response;
import com.epam.library.pool.ConnectionPool;
import com.epam.library.pool.ConnectionPoolException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by test on 19.03.2017.
 */
public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Controller controller = new Controller();

        ConnectionPool pool;
        try {
            pool = ConnectionPool.getInstance();
            pool.init();
            logger.log(Level.INFO, "ConnectionPool was initialized");
        } catch (ConnectionPoolException e) {
            logger.log(Level.FATAL, e);
            throw new RuntimeException(e);
        }

        Request request = new Request();
        request.setCommandName(CommandName.GENERATE_EMPLOYEE_BOOK_LIST);
        Response response = controller.processRequest(request);
        System.out.println(response.getMessage());

        Request request1 = new Request();
        request1.setCommandName(CommandName.REPORT_ABOUT_MORE_THAN_ONE_BOOK_READ);
        Response response1 = controller.processRequest(request1);
        LinkedHashMap<String, Integer> goodReaders = response1.getVeryGoodReaders();
        printGoodReaders(goodReaders);

        Request request2 = new Request();
        request2.setCommandName(CommandName.REPORT_ABOUT_LESS_OR_EQUAL_TWO_BOOK_READ);
        Response response2 = controller.processRequest(request2);
        LinkedHashMap<Employee, Integer> notGoodReaders = response2.getNotGoodReaders();
        printNotSoGoodReaders(notGoodReaders);

        Request request3 = new Request();
        request3.setCommandName(CommandName.RENAME_BOOK);
        request3.setOldTitle("Hibernate*");
        request3.setNewTitle("Hibernate JBoss");
        Response response3 = controller.processRequest(request3);
        String message = response3.getMessage();
        System.out.println(message);

        pool.destroyPool();
        logger.log(Level.INFO, "ConnectionPool was destroyed");
    }

    private static void printNotSoGoodReaders(LinkedHashMap<Employee, Integer> notGoodReaders) {
        if(notGoodReaders!= null){
            System.out.println("List of readers who read less than 3 book: ");
            for (Map.Entry<Employee, Integer> entry : notGoodReaders.entrySet()) {
                String name = entry.getKey().getName()+ ", ";
                if(entry.getValue() < 2){
                    name = "";
                }
                System.out.println(name + entry.getKey().getDateOfBirth() + ": " + entry.getValue());
            }
        } else {
            System.out.println("No one read less than 3 book. ");
        }

    }

    private static void printGoodReaders(LinkedHashMap<String, Integer> goodReaders) {
        if(goodReaders!= null){
            System.out.println("List of readers who read more than 1 book: ");
            for (Map.Entry<String, Integer> entry : goodReaders.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } else {
            System.out.println("No one read more than 1 book: ");
        }

    }
}
