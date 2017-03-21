package com.epam.library.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Created by test on 19.03.2017.
 */
public class Response implements Cloneable, Serializable {

    private LinkedHashMap<Employee, Integer> notGoodReaders;
    private LinkedHashMap<String, Integer> veryGoodReaders;
    private boolean operationStatus;
    private String message;
    private Book book;

    public LinkedHashMap<Employee, Integer> getNotGoodReaders() {
        return notGoodReaders;
    }

    public void setNotGoodReaders(LinkedHashMap<Employee, Integer> notGoodReaders) {
        this.notGoodReaders = notGoodReaders;
    }

    public LinkedHashMap<String, Integer> getVeryGoodReaders() {
        return veryGoodReaders;
    }

    public void setVeryGoodReaders(LinkedHashMap<String, Integer> veryGoodReaders) {
        this.veryGoodReaders = veryGoodReaders;
    }

    public boolean isOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(boolean operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        if (operationStatus != response.operationStatus) return false;
        if (notGoodReaders != null ? !notGoodReaders.equals(response.notGoodReaders) : response.notGoodReaders != null)
            return false;
        if (veryGoodReaders != null ? !veryGoodReaders.equals(response.veryGoodReaders) : response.veryGoodReaders != null)
            return false;
        if (message != null ? !message.equals(response.message) : response.message != null) return false;
        return book != null ? book.equals(response.book) : response.book == null;

    }

    @Override
    public int hashCode() {
        int result = notGoodReaders != null ? notGoodReaders.hashCode() : 0;
        result = 31 * result + (veryGoodReaders != null ? veryGoodReaders.hashCode() : 0);
        result = 31 * result + (operationStatus ? 1 : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (book != null ? book.hashCode() : 0);
        return result;
    }
}
