package br.com.farias.rest_with_spring_boot_and_java.file.exporter.contract;

import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface PersonExporter {

    Resource exportPeople(List<PersonDTO> people) throws Exception;
    Resource exportPerson(PersonDTO person) throws Exception;
}
