package br.com.farias.rest_with_spring_boot_and_java.integrationtests.controllers.withxml;

import br.com.farias.rest_with_spring_boot_and_java.config.TestConfigs;
//import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.PersonDTO;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper xmlMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = buildSpecification();

        // Serializando o objeto para XML manualmente
        String xmlBody = xmlMapper.writeValueAsString(person);

        var content = given().spec(specification)
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(xmlBody) // Enviando a string XML, garantindo que o Jackson seja usado
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = xmlMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson, "O objeto PersonDTO não deveria ser nulo");
        assertNotNull(createdPerson.getId(), "O ID da pessoa não deveria ser nulo");
        assertTrue(createdPerson.getId() > 0);
        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("Benedict Torvalds");

        String xmlBody = xmlMapper.writeValueAsString(person);

        var content = given().spec(specification)
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(xmlBody) // Enviando a string XML
                .when()
                .put("{id}", person.getId())
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO updatedPerson = xmlMapper.readValue(content, PersonDTO.class);
        person = updatedPerson;

        assertNotNull(updatedPerson);
        assertEquals("Benedict Torvalds", updatedPerson.getLastName());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        var content = given().spec(specification)
                .accept(ContentType.XML)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO foundPerson = xmlMapper.readValue(content, PersonDTO.class);
        assertNotNull(foundPerson);
        assertEquals(person.getId(), foundPerson.getId());
        assertEquals("Benedict Torvalds", foundPerson.getLastName());
    }

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {
        var content = given().spec(specification)
                .accept(ContentType.XML)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO disabledPerson = xmlMapper.readValue(content, PersonDTO.class);
        person = disabledPerson;

        assertNotNull(disabledPerson);
        assertFalse(disabledPerson.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTest() {
        given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {
        var content = given().spec(specification)
                .accept(ContentType.XML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<PersonDTO> people = xmlMapper.readValue(content, new TypeReference<>() {});

        assertNotNull(people);

        PersonDTO personOne = people.get(0);
        assertEquals(1, personOne.getId());
        assertEquals("Marcelo", personOne.getFirstName().trim()); // Correção na asserção
        assertEquals("Farias", personOne.getLastName());

        PersonDTO personFour = people.get(3);
        assertEquals(5, personFour.getId());
        assertEquals("Luciana ", personFour.getFirstName());
        assertEquals("Andrade", personFour.getLastName());
    }

    private RequestSpecification buildSpecification() {
        return new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_AGSUS)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }
}