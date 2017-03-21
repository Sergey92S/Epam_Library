package com.epam.library.domain;

import com.epam.library.command.CommandName;

import java.io.Serializable;

/**
 * Created by test on 19.03.2017.
 */
public class Request implements Cloneable, Serializable {

    private CommandName commandName;
    private String oldTitle;
    private String newTitle;
    private String bookId;
    private Book book;

    public Request(){
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public CommandName getCommandName() {
        return commandName;
    }

    public void setCommandName(CommandName commandName) {
        this.commandName = commandName;
    }

    public String getOldTitle() {
        return oldTitle;
    }

    public void setOldTitle(String oldTitle) {
        this.oldTitle = oldTitle;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (commandName != null ? !commandName.equals(request.commandName) : request.commandName != null) return false;
        if (oldTitle != null ? !oldTitle.equals(request.oldTitle) : request.oldTitle != null) return false;
        if (newTitle != null ? !newTitle.equals(request.newTitle) : request.newTitle != null) return false;
        return bookId != null ? bookId.equals(request.bookId) : request.bookId == null;

    }

    @Override
    public int hashCode() {
        int result = commandName != null ? commandName.hashCode() : 0;
        result = 31 * result + (oldTitle != null ? oldTitle.hashCode() : 0);
        result = 31 * result + (newTitle != null ? newTitle.hashCode() : 0);
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        return result;
    }
}
