package br.com.farias.rest_with_spring_boot_and_java.controllers;

import br.com.farias.rest_with_spring_boot_and_java.config.WebConstants;
import br.com.farias.rest_with_spring_boot_and_java.controllers.docs.BookControllerDocs;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.BookDTO;
import br.com.farias.rest_with_spring_boot_and_java.services.BookServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class BookController implements BookControllerDocs {

    @Autowired
    private BookServices services;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML})
    @Override
    public List<BookDTO> findAll() {
        return services.findAll();
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML}
    )
    @Override
    public BookDTO findById(@PathVariable("id") Long id) {
        return services.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        services.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML}
    )
    @Override
    public BookDTO create(@RequestBody BookDTO book) {
        return services.create(book);
    }

    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    WebConstants.APPLICATION_YML},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    WebConstants.APPLICATION_YML}
    )
    @Override
    public BookDTO update(@RequestBody BookDTO book) {
        return services.update(book);
    }
}
