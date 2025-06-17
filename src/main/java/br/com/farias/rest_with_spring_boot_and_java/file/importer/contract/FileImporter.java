package br.com.farias.rest_with_spring_boot_and_java.file.importer.contract;

import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
