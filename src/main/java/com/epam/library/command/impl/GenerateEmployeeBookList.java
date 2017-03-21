package com.epam.library.command.impl;

import com.epam.library.command.Command;
import com.epam.library.domain.Request;
import com.epam.library.domain.Response;
import com.epam.library.service.LibraryService;
import com.epam.library.service.ServiceException;
import com.epam.library.service.impl.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by test on 19.03.2017.
 */
public class GenerateEmployeeBookList implements Command {
    private static Logger logger = LogManager.getLogger(GenerateEmployeeBookList.class);


    private static final String SUCCESS_MESSAGE = "EmployeeBook list was successfully generated!";
    private static final String FAIL_MESSAGE = "EmployeeBook list wasn't generated!";

    @Override
    public Response execute(Request request) {
        ServiceFactory factory = ServiceFactory.getInstance();
        LibraryService service = factory.getLibraryService();
        boolean isGenerated = false;

        try {
            isGenerated = service.generateEmployeeBookList();
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
        }
        Response response = new Response();
        if (isGenerated) {
            response.setMessage(SUCCESS_MESSAGE);
        } else {
            response.setMessage(FAIL_MESSAGE);
        }

        return response;
    }
}