package com.epam.library.command.impl;

import com.epam.library.command.Command;
import com.epam.library.domain.Request;
import com.epam.library.domain.Response;
import com.epam.library.service.LibraryService;
import com.epam.library.service.impl.ServiceFactory;

/**
 * Created by test on 19.03.2017.
 */
public class DeleteBookById implements Command {

    @Override
    public Response execute(Request request) {
        ServiceFactory factory = ServiceFactory.getInstance();
        LibraryService service = factory.getLibraryService();
        return null;
    }
}
