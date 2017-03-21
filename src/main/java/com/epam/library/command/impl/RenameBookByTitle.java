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
public class RenameBookByTitle implements Command {
    private static Logger logger = LogManager.getLogger(RenameBookByTitle.class);

    private static final String SUCCESS_MESSAGE = "There were renamed several books.";
    private static final String FAIL_MESSAGE = "Books wasn't renamed!";

    @Override
    public Response execute(Request request) {
        ServiceFactory factory = ServiceFactory.getInstance();
        LibraryService service = factory.getLibraryService();
        boolean renamed = false;

        try{
            renamed = service.renameBookById(request.getOldTitle(), request.getNewTitle());
        } catch (ServiceException e){
            logger.log(Level.ERROR, e);
        }
        Response response = new Response();
        if (renamed) {
            response.setMessage(SUCCESS_MESSAGE);
        } else {
            response.setMessage(FAIL_MESSAGE);
        }

        return response;
    }
}
