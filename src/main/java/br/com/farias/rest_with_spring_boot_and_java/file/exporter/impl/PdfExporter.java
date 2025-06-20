package br.com.farias.rest_with_spring_boot_and_java.file.exporter.impl;

import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;
import br.com.farias.rest_with_spring_boot_and_java.file.exporter.contract.PersonExporter;
import br.com.farias.rest_with_spring_boot_and_java.services.QRCodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements PersonExporter {

    @Autowired
    private QRCodeService service;

    @Override
    public Resource exportPeople(List<PersonDTO> people) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/templates/people.jrxml");
        if (inputStream == null) {
            throw new RuntimeException("Template file not found: /templates/people.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
        Map<String, Object> parameters = new HashMap<>();
        // parameters.put("title", "People Report");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws Exception {
        // 1. Carrega o template principal a partir do classpath (deve estar em src/main/resources/templates/)
        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/person.jrxml");
        if (mainTemplateStream == null) {
            throw new RuntimeException("Arquivo de template principal não encontrado em: src/main/resources/templates/person.jrxml");
        }

        // 2. Carrega o template do sub-relatório a partir do classpath
        InputStream subReportStream = getClass().getResourceAsStream("/templates/books.jrxml");
        if (subReportStream == null) {
            throw new RuntimeException("Arquivo de sub-relatório não encontrado em: src/main/resources/templates/books.jrxml");
        }

        // 3. Compila ambos os relatórios
        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        // 4. Prepara a fonte de dados para o sub-relatório (a lista de livros)
        JRBeanCollectionDataSource subReportDataSource = new JRBeanCollectionDataSource(person.getBooks());

        // 5. Gera o QR Code
        InputStream qrCodeStream = service.generateQRCode(person.getProfileUrl(), 200, 200);

        // 6. Prepara os parâmetros para o relatório principal
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("QR_CODEIMAGE", qrCodeStream);
        parameters.put("BOOK_SUB_REPORT", subReport); // Passando o sub-relatório JÁ COMPILADO
        parameters.put("SUB_REPORT_DATA_SOURCE", subReportDataSource); // Passando a fonte de dados do sub-relatório

        // 7. Prepara a fonte de dados principal (a própria pessoa, em uma lista)
        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));

        // 8. Preenche o relatório
        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, mainDataSource);

        // 9. Exporta para PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }
}
