package br.com.farias.rest_with_spring_boot_and_java.controllers;

import br.com.farias.rest_with_spring_boot_and_java.config.WebConstants;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;

import br.com.farias.rest_with_spring_boot_and_java.controllers.docs.PersonControllerDocs;
import br.com.farias.rest_with_spring_boot_and_java.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonServices services;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML})
    @Override
    public List<PersonDTO> findAll() {
        return services.findAll();
    }

    //@CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML}
    )
    @Override
    public PersonDTO findById(@PathVariable("id") Long id) {
        return services.findById(id);
    }


    //@CrossOrigin(origins = "http://localhost:8080")
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML}
    )
    @Override
    public PersonDTO create(@RequestBody PersonDTO person) {
        return services.create(person);
    }

    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML}
    )
    @Override
    public PersonDTO update(@RequestBody PersonDTO person) {
        return services.update(person);
    }

    @PatchMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML}
    )
    @Override
    public PersonDTO disablePerson(@PathVariable("id") Long id) {
        return services.disablePerson(id);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        services.delete(id);
        return ResponseEntity.noContent().build();
    }
}
