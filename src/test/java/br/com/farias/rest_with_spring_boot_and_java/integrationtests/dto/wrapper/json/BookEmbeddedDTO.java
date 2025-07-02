package br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.wrapper.json;

import br.com.farias.rest_with_spring_boot_and_java.data.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class BookEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("book")
    private List<BookDTO> book;

    public BookEmbeddedDTO() {}

    public List<BookDTO> getBooks() {
        return book;
    }

    public void setBook(List<BookDTO> books) {
        this.book = books;
    }
}
