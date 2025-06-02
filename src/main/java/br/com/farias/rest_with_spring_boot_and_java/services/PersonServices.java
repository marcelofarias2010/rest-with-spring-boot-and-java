package br.com.farias.rest_with_spring_boot_and_java.services;

//import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;
//import br.com.farias.rest_with_spring_boot_and_java.data.dto.v2.PersonDTOV2;
//import br.com.farias.rest_with_spring_boot_and_java.data.dto.v3.PersonDTO;

import br.com.farias.rest_with_spring_boot_and_java.controllers.PersonController;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.v4.PersonDTO;
import br.com.farias.rest_with_spring_boot_and_java.exception.ResourceNotFoundException;

import static br.com.farias.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseListObjects;
import static br.com.farias.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;

import br.com.farias.rest_with_spring_boot_and_java.mapper.custom.PersonMapper;
import br.com.farias.rest_with_spring_boot_and_java.model.Person;
import br.com.farias.rest_with_spring_boot_and_java.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper converter;


    public List<PersonDTO> findAll() {

        logger.info("Finding all People!");

        var persons = parseListObjects(repository.findAll(), PersonDTO.class);
        persons.forEach(this::addHateoasLinks);

        return persons;
    }

    /**
     * // versão v1,v2,v3
     * public PersonDTO findById(Long id) {
     * logger.info("Finding one Person!");
     * <p>
     * var entity = repository.findById(id)
     * .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
     * return parseObject(entity, PersonDTO.class);
     * }
     */
    // versão v4
    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO create(PersonDTO person) {

        logger.info("Creating one Person!");
        var entity = parseObject(person, Person.class);

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

//    public PersonDTOV2 createV2(PersonDTOV2 person) {
//
//        logger.info("Creating one Person v2!");
//        var entity = converter.convertDTOtoEntity(person);
//
//        return converter.convertEntityToDTO(repository.save(entity));
//    }

    public PersonDTO update(PersonDTO person) {

        logger.info("Updating one Person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public void delete(Long id) {

        logger.info("Deleting one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto.getId(),dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
