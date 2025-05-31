package br.com.farias.rest_with_spring_boot_and_java.controllers;

//import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;
//import br.com.farias.rest_with_spring_boot_and_java.data.dto.v2.PersonDTOV2;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.v3.PersonDTO;
import br.com.farias.rest_with_spring_boot_and_java.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/person/v3")
public class PersonController {

    @Autowired
    private PersonServices services;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE})
    public List<PersonDTO> findAll() { return services.findAll(); }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE}
    )
    public PersonDTO findById(@PathVariable("id") Long id) { return services.findById(id);
//        var person = services.findById(id);
//        person.setBirthDay(new Date());
//        person.setPhoneNumber("+55 (61) 98659-8515");
//        person.setSensitiveData("Foo Bar");
//        return person;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        services.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE}
    )
    public PersonDTO create(@RequestBody PersonDTO person) {
        return services.create(person);
    }

    @PostMapping(value = "/v2",
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE}
    )
//    public PersonDTOV2 create(@RequestBody PersonDTOV2 person) {
//        return services.createV2(person);
//    }

    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE}
    )
    public PersonDTO update(@RequestBody PersonDTO person) {
        return services.update(person);
    }
}
