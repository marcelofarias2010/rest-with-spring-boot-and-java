package br.com.farias.rest_with_spring_boot_and_java.integrationtests.controllers.withyaml;

import br.com.farias.rest_with_spring_boot_and_java.config.TestConfigs;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.wrapper.yaml.PersonCollectionWrapper;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper yamlMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        yamlMapper = new YAMLMapper();
        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void createTest() {
        mockPerson();
        specification = buildSpecification();

        PersonDTO createdPerson = given(specification)
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .queryParam("mediaType", "yaml") // Adicionando o parâmetro
                .body(person, yamlMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDTO.class, yamlMapper);

        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);
        assertEquals("Linus", createdPerson.getFirstName());
    }

    @Test
    @Order(2)
    void updateTest() {
        person.setLastName("Benedict Torvalds");

        PersonDTO updatedPerson = given(specification)
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .queryParam("mediaType", "yaml") // Adicionando o parâmetro
                .body(person, yamlMapper)
                .when()
                .put("{id}", person.getId())
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDTO.class, yamlMapper);

        person = updatedPerson;

        assertNotNull(updatedPerson);
        assertEquals("Benedict Torvalds", updatedPerson.getLastName());
    }

    @Test
    @Order(3)
    void findByIdTest() {
        specification = buildSpecification();
        PersonDTO foundPerson = given(specification)
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .queryParam("mediaType", "yaml") // Adicionando o parâmetro
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDTO.class, yamlMapper);

        person = foundPerson;

        assertNotNull(foundPerson);
        assertEquals(person.getId(), foundPerson.getId());
        assertEquals("Benedict Torvalds", foundPerson.getLastName());
    }

    @Test
    @Order(4)
    void disableTest() {
        PersonDTO disabledPerson = given(specification)
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .queryParam("mediaType", "yaml") // Adicionando o parâmetro
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDTO.class, yamlMapper);

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
    //@Disabled("REASON: Still Under Development")
    void findAllTest() throws JsonProcessingException {
        specification = buildSpecification();

        var content = given(specification)
                .config(config)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                // Adicionando parâmetros de paginação para garantir uma resposta consistente
                .queryParam("page", 0)
                .queryParam("size", 12)
                .queryParam("direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // Usando o PersonCollectionWrapper que corresponde à resposta real da API
        PersonCollectionWrapper wrapper = yamlMapper.getMapper().readValue(content, PersonCollectionWrapper.class);

        List<PersonDTO> people = wrapper.getContent();

        assertNotNull(people, "A lista de pessoas não deveria ser nula");

        PersonDTO personOne = people.get(0);
        assertEquals(701, personOne.getId());
        assertEquals("Abba", personOne.getFirstName());
        assertEquals("Bonellie", personOne.getLastName());
        assertEquals("Suite 35", personOne.getAddress());
        assertTrue(personOne.getEnabled());

        PersonDTO personFour = people.get(3);
        assertEquals(388, personFour.getId());
        assertEquals("Adara", personFour.getFirstName());
        assertEquals("Lehmann", personFour.getLastName());
        assertEquals("PO Box 24269", personFour.getAddress());
        assertTrue(personFour.getEnabled());
    }

    @Test
    @Order(6)
    void findByNameTest() throws JsonProcessingException {
        specification = buildSpecification();

        var content = given(specification)
                .config(config)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                // Adicionando parâmetros de paginação para garantir uma resposta consistente
                .pathParam("firstName","and")
                .queryParam("page",0,"size",12,"direction","asc")
                .when()
                .get("findPeopleByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // Usando o PersonCollectionWrapper que corresponde à resposta real da API
        PersonCollectionWrapper wrapper = yamlMapper.getMapper().readValue(content, PersonCollectionWrapper.class);

        List<PersonDTO> people = wrapper.getContent();

        assertNotNull(people, "A lista de pessoas não deveria ser nula");

        PersonDTO personOne = people.get(0);
        assertEquals(163, personOne.getId());
        assertEquals("Alisander", personOne.getFirstName());
        assertEquals("Currell", personOne.getLastName());
        assertEquals("Suite 98", personOne.getAddress());
        assertTrue(personOne.getEnabled());

        PersonDTO personFour = people.get(3);
        assertEquals(74, personFour.getId());
        assertEquals("Andrew", personFour.getFirstName());
        assertEquals("Ondrich", personFour.getLastName());
        assertEquals("Suite 80", personFour.getAddress());
        assertFalse(personFour.getEnabled());
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
