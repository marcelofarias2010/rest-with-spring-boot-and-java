package br.com.farias.rest_with_spring_boot_and_java.integrationtests.controllers.withyaml;

import br.com.farias.rest_with_spring_boot_and_java.config.TestConfigs;

import br.com.farias.rest_with_spring_boot_and_java.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.AccountCredentialsDTO;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.TokenDTO;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.wrapper.xml.PagedModelPerson;
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
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper yamlMapper;
    private static PersonDTO person;
    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUpTest() {
        yamlMapper = new YAMLMapper();
        person = new PersonDTO();
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(0)
    void signin() throws JsonProcessingException {
        AccountCredentialsDTO credentials =
                new AccountCredentialsDTO("leandro", "admin123");

        tokenDto = given()
                .config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig().
                                                encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT))
                )
                .basePath("/auth/signin")
                //.port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .body(credentials, yamlMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, yamlMapper);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_AGSUS)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
                .setBasePath("/api/person/v1")
                //.setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(1)
    void createTest() {
        mockPerson();
        var createdPerson = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig().
                                                encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT))
                ).spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .body(person, yamlMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .extract()
                .body()
                .as(PersonDTO.class, yamlMapper);

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
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
    void findByIdTest() throws JsonProcessingException {
        var createdPerson = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig().
                                                encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT))
                ).spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .extract()
                .body()
                .as(PersonDTO.class, yamlMapper);

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
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
    void findAllTest() throws JsonProcessingException {

        var response = given(specification)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .extract()
                .body()
                .as(PagedModelPerson.class, yamlMapper);

        List<PersonDTO> people = response.getContent();

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Alvera", personOne.getFirstName());
        assertEquals("McCaughey", personOne.getLastName());
        assertEquals("Suite 94", personOne.getAddress());
        assertEquals("Female", personOne.getGender());
        assertFalse(personOne.getEnabled());

        PersonDTO personFour = people.get(4);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Alyssa", personFour.getFirstName());
        assertEquals("O' Mullane", personFour.getLastName());
        assertEquals("Apt 1679", personFour.getAddress());
        assertEquals("Female", personFour.getGender());
        assertTrue(personFour.getEnabled());
    }

    @Test
    @Order(7)
    void findByNameTestTest() throws JsonProcessingException {

        var response = given(specification)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .pathParam("firstName", "and")
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("findPeopleByName/{firstName}")
                .then()
                .statusCode(200)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .extract()
                .body()
                .as(PagedModelPerson.class, yamlMapper);

        List<PersonDTO> people = response.getContent();

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Alisander", personOne.getFirstName());
        assertEquals("Currell", personOne.getLastName());
        assertEquals("Suite 98", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTO personFour = people.get(4);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Andrey", personFour.getFirstName());
        assertEquals("Sighart", personFour.getLastName());
        assertEquals("Suite 51", personFour.getAddress());
        assertEquals("Male", personFour.getGender());
        assertTrue(personFour.getEnabled());
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

    @Test
    @Order(8)
    void hateoasAndHalTest() throws JsonProcessingException {

        Response response = given(specification)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .extract()
                .response();

        // Retrieves the response body as a YAML string
        String yaml = response.getBody().asString();

        // Uses SnakeYAML to parse the YAML
        Yaml yamlParser = new Yaml();
        Map<String, Object> parsedYaml = yamlParser.load(yaml);

        // Validates the content
        List<Map<String, Object>> content = (List<Map<String, Object>>) parsedYaml.get("content");

        // Iterates through each person in the content
        for (Map<String, Object> person : content) {

            List<Map<String, String>> links = (List<Map<String, String>>) person.get("links");
            // Iterates through each link in the person's links
            for (Map<String, String> link : links) {
                // Checks if the link has the expected attributes
                assertThat("HATEOAS/HAL link rel is missing", link, hasKey("rel"));
                assertThat("HATEOAS/HAL link href is missing", link, hasKey("href"));
                assertThat("HATEOAS/HAL link type is missing", link, hasKey("type"));

                // Validates the format of the link
                assertThat("HATEOAS/HAL link " + link + " has an invalid URL", link.get("href"), matchesPattern("https?://.+/api/person/v1.*"));
            }
        }

        // Validates pagination attributes
        Map<String, Object> page = (Map<String, Object>) parsedYaml.get("page");
        assertThat("Page number is incorrect", page.get("number"), is(3));
        assertThat("Page size is incorrect", page.get("size"), is(12));

        // Validates the total number of elements and pages
        Integer totalElements = Integer.parseInt(page.get("totalElements").toString());
        Integer totalPages = Integer.parseInt(page.get("totalPages").toString());

        assertTrue("totalElements should be greater than 0", totalElements > 0);
        assertTrue("totalPages should be greater than 0", totalPages > 0);

        // Validates the navigation links of the page
        List<Map<String, String>> pageLinks = (List<Map<String, String>>) parsedYaml.get("links");
        for (Map<String, String> pageLink : pageLinks) {

            // Checks if the page link contains the href attribute
            assertThat("HATEOAS/HAL page link href is missing", pageLink, hasKey("href"));

            // Validates the format of the page link URL
            assertThat("HATEOAS/HAL page link " + pageLink + " has an invalid URL", pageLink.get("href"), matchesPattern("https?://.+/api/person/v1.*"));
        }
    }

    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
