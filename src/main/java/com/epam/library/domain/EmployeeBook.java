package com.epam.library.domain;

import java.io.Serializable;

/**
 * Created by test on 19.03.2017.
 */
public class EmployeeBook implements Cloneable, Serializable{

    private int id;
    private int bookId;
    private int employeeId;

    public EmployeeBook(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeBook that = (EmployeeBook) o;

        if (id != that.id) return false;
        if (bookId != that.bookId) return false;
        return employeeId == that.employeeId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + bookId;
        result = 31 * result + employeeId;
        return result;
    }

    @Override
    public String toString() {
        return "EmployeeBook{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", employeeId=" + employeeId +
                "} ";
    }
}