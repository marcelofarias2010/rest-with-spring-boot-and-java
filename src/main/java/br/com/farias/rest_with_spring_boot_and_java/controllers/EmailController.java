package br.com.farias.rest_with_spring_boot_and_java.controllers;

import br.com.farias.rest_with_spring_boot_and_java.controllers.docs.EmailControllerDocs;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.request.EmailRequestDTO;
import br.com.farias.rest_with_spring_boot_and_java.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService service;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        service.sendSimpleEmail(emailRequest);
        return new ResponseEntity<>("e-Mail sent with success!", HttpStatus.OK);
    }

    @PostMapping(value = "/withAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("emailRequest") String emailRequest,
            @RequestParam("attachment") MultipartFile attachment)
    {
        service.setEmailWithAttachment(emailRequest, attachment);
        return new ResponseEntity<>("e-Mail with attachment sent successfully!", HttpStatus.OK);
    }
}
