package br.com.farias.rest_with_spring_boot_and_java.services;

import br.com.farias.rest_with_spring_boot_and_java.exception.ResourceNotFoundException;
import br.com.farias.rest_with_spring_boot_and_java.model.Person;
import br.com.farias.rest_with_spring_boot_and_java.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;


    public List<Person> findAll() {
        logger.info("Finding one Person!");
//        List<Person> persons = new ArrayList<Person>();
//        for (int i = 0; i < 8; i++) {
//            Person person = mockPerson(i);
//            persons.add(person);
//        }
//        return persons;

        return repository.findAll();

    }

    public Person findById(Long id) {
        logger.info("Finding all Peaple!");

//        Person person = new Person();
//        person.setId(counter.incrementAndGet());
//        person.setFirstName("Marcelo");
//        person.setLastName("Farias");
//        person.setAddress("CeiLândia - Brasília - Brasil");
//        person.setGender("Male");
//        return person;

        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
    }

    private Person mockPerson(int i) {
        Person person = new Person();

        person.setId(counter.incrementAndGet());
        person.setFirstName("Firstname "+i);
        person.setLastName("Lastname "+i);
        person.setAddress("Some Address in Brasil");
        person.setGender("Male");
        return person;
    }

    public Person create(Person person){
        logger.info("Creating one Person!");

        return repository.save(person);
    }

    public Person update(Person person){
        logger.info("Updating one Person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(person);
    }

    public void delete(Long id){
        logger.info("Deleting one Person!");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        repository.delete(entity);
    }
}
