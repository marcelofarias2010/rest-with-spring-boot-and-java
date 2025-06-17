package br.com.farias.rest_with_spring_boot_and_java.file.exporter.contract;

import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
}
