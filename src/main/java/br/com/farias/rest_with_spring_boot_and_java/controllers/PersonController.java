package br.com.farias.rest_with_spring_boot_and_java.controllers;

//import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;
//import br.com.farias.rest_with_spring_boot_and_java.data.dto.v2.PersonDTOV2;
//import br.com.farias.rest_with_spring_boot_and_java.data.dto.v3.PersonDTO;

import br.com.farias.rest_with_spring_boot_and_java.controllers.docs.PersonControllerDocs;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.v4.PersonDTO;
import br.com.farias.rest_with_spring_boot_and_java.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonServices services;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<PersonDTO> findAll() {
        return services.findAll();
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public PersonDTO findById(@PathVariable("id") Long id) {
        return services.findById(id);
//        var person = services.findById(id);
//        person.setBirthDay(new Date());
//        person.setPhoneNumber("+55 (61) 98659-8515");
//        person.setSensitiveData("Foo Bar");
//        return person;
    }


    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        services.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public PersonDTO create(@RequestBody PersonDTO person) {
        return services.create(person);
    }

//    @PostMapping(value = "/v2",
//            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE}
//    )
//    public PersonDTOV2 create(@RequestBody PersonDTOV2 person) {
//        return services.createV2(person);
//    }

    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public PersonDTO update(@PathVariable("id") Long id, @RequestBody PersonDTO person) {
        // Setar o ID no DTO explicitamente!
        person.setId(id);
        return services.update(person);
    }
}
