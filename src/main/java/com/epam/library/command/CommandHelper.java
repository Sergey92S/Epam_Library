package com.epam.library.command;

import com.epam.library.command.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by test on 19.03.2017.
 */
public class CommandHelper {
    public static CommandHelper getInstance() {
        return INSTANCE;
    }

    private Map<CommandName, Command> commands = new HashMap<>();

    private static final CommandHelper INSTANCE = new CommandHelper();

    private CommandHelper(){
        commands.put(CommandName.CREATE_NEW_BOOK, new CreateNewBook());
        commands.put(CommandName.DELETE_BOOK_BY_ID, new DeleteBookById());
        commands.put(CommandName.SHOW_BOOK_BY_ID, new ShowBookById());
        commands.put(CommandName.RENAME_BOOK, new RenameBookByTitle());
        commands.put(CommandName.REPORT_ABOUT_MORE_THAN_ONE_BOOK_READ, new ReportAboutGoodReaders());
        commands.put(CommandName.REPORT_ABOUT_LESS_OR_EQUAL_TWO_BOOK_READ, new ReportAboutLittleReaders());
        commands.put(CommandName.GENERATE_EMPLOYEE_BOOK_LIST, new GenerateEmployeeBookList());

    }

    public Command getCommand(CommandName commandName){
        return commands.get(commandName);
    }
}
