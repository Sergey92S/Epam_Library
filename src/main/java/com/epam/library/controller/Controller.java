package com.epam.library.controller;

import com.epam.library.command.Command;
import com.epam.library.command.CommandHelper;
import com.epam.library.command.CommandName;
import com.epam.library.domain.Request;
import com.epam.library.domain.Response;

/**
 * Created by test on 19.03.2017.
 */
public class Controller {
    public Controller(){
    }

    public Response processRequest(Request request) {
        CommandName commandName = request.getCommandName();
        Command command = CommandHelper.getInstance().getCommand(commandName);
        return command.execute(request);
    }
}
