package com.epam.library.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by test on 19.03.2017.
 */
public class Book implements Cloneable, Serializable{

    /**
     * book's id number
     */
    private int id;

    /**
     * title of book
     */
    private String title;

    /**
     * brief of book
     */
    private String brief;

    /**
     * date when book was published
     */
    private Date publishYear;

    public Book(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Date getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Date publishYear) {
        this.publishYear = publishYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (title != null ? !title.equals(book.title) : book.title != null) return false;
        if (brief != null ? !brief.equals(book.brief) : book.brief != null) return false;
        return publishYear != null ? publishYear.equals(book.publishYear) : book.publishYear == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (brief != null ? brief.hashCode() : 0);
        result = 31 * result + (publishYear != null ? publishYear.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", brief='" + brief + '\'' +
                ", publishYear=" + publishYear +
                "} ";
    }
}
