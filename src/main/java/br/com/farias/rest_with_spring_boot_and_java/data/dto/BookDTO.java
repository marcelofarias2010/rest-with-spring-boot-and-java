package br.com.farias.rest_with_spring_boot_and_java.data.dto;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class BookDTO extends RepresentationModel<BookDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long Id;
    private String author;
    private Date launchDate;
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

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
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
        if (!super.equals(o)) return false;
        BookDTO bookDTO = (BookDTO) o;
        return Double.compare(price, bookDTO.price) == 0 && Objects.equals(Id, bookDTO.Id) && Objects.equals(author, bookDTO.author) && Objects.equals(launchDate, bookDTO.launchDate) && Objects.equals(title, bookDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Id, author, launchDate, price, title);
    }
}
