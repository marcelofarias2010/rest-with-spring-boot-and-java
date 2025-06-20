package br.com.farias.rest_with_spring_boot_and_java.controllers;

import br.com.farias.rest_with_spring_boot_and_java.config.WebConstants;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;

import br.com.farias.rest_with_spring_boot_and_java.controllers.docs.PersonControllerDocs;
import br.com.farias.rest_with_spring_boot_and_java.file.exporter.MediaTypes;
import br.com.farias.rest_with_spring_boot_and_java.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonServices services;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(services.findAll(pageable));
    }

    @GetMapping(
            value = "/exportPage",
            produces = {
                    MediaTypes.APPLICATION_XLSX_VALUE,
                    MediaTypes.APPLICATION_CSV_VALUE,
                    MediaTypes.APPLICATION_PDF_VALUE
            }
    )
    @Override
    public ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, HttpServletRequest request

    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection, "firstName"));
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = services.exportPage(pageable,acceptHeader);

        Map<String, String> extensionMap = Map.of(
                MediaTypes.APPLICATION_XLSX_VALUE, ".xlsx",
                MediaTypes.APPLICATION_CSV_VALUE, ".csv",
                MediaTypes.APPLICATION_PDF_VALUE, ".pdf"
        );

        var fileExtension = extensionMap.getOrDefault(acceptHeader,"");
        var contentType = acceptHeader != null ? acceptHeader : "application/octet-stream";
        var filename = "people_exported" + fileExtension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(file);
    }

    @GetMapping(
            value = "/findPeopleByName/{firstName}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(services.findByName(firstName,pageable));
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

    @GetMapping(
            value = "/export/{id}",
            produces = { MediaTypes.APPLICATION_PDF_VALUE }
    )
    @Override
    public ResponseEntity<Resource> export(@PathVariable("id") Long id, HttpServletRequest request) {
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = services.exportPerson(id,acceptHeader);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(acceptHeader))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=person.pdf")
                .body(file);
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

    @PostMapping(
            value = "/massCreation",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, WebConstants.APPLICATION_YML}
    )
    @Override
    public List<PersonDTO> massCreation(@RequestParam("file") MultipartFile file) {
        return services.massCreation(file);
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
