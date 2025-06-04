package br.com.farias.rest_with_spring_boot_and_java.data.dto;

import br.com.farias.rest_with_spring_boot_and_java.model.Book;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class BookDTO extends RepresentationModel<BookDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long Id;
    private String author;
    private Date lounchDate;
    private double price;
    private String title;

    public BookDTO(){}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLounchDate() {
        return lounchDate;
    }

    public void setLounchDate(Date lounchDate) {
        this.lounchDate = lounchDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return Double.compare(price, bookDTO.price) == 0 && Objects.equals(Id, bookDTO.Id) && Objects.equals(author, bookDTO.author) && Objects.equals(lounchDate, bookDTO.lounchDate) && Objects.equals(title, bookDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, author, lounchDate, price, title);
    }
}
