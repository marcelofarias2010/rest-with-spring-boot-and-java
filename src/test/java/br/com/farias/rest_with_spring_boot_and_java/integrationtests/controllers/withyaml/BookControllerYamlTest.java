package br.com.farias.rest_with_spring_boot_and_java.integrationtests.controllers.withyaml;

import br.com.farias.rest_with_spring_boot_and_java.config.TestConfigs;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.AccountCredentialsDTO;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.BookDTO;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.TokenDTO;
import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.wrapper.xml.PagedModelBook;
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
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@Nested
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;

    private static BookDTO book;
    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUpTest() {
        objectMapper = new YAMLMapper();
        book = new BookDTO();
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
                .body(credentials, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, objectMapper);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_AGSUS)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
                .setBasePath("/api/book/v1")
                //.setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockBook();

        var createdBook = given().config(
                RestAssuredConfig.config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig().
                            encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT))
                ).spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YAML)
            .accept(TestConfigs.CONTENT_TYPE_YAML)
                .body(book, objectMapper)
            .when()
                .post()
            .then()
                .statusCode(200)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
            .extract()
                .body()
                    .as(BookDTO.class, objectMapper);

        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(book.getId());
        assertEquals("Docker Deep Dive", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(55.99, book.getPrice());

    }
    
    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {

        book.setTitle("Docker Deep Dive - Updated");

        var createdBook = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig().
                                                encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT))
                ).spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YAML)
            .accept(TestConfigs.CONTENT_TYPE_YAML)
                .body(book, objectMapper)
            .when()
                .put()
            .then()
                .statusCode(200)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
            .extract()
                .body()
                .as(BookDTO.class, objectMapper);

        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(book.getId());
        assertEquals("Docker Deep Dive - Updated", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(55.99, book.getPrice());

    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var createdBook = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig().
                                                encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT))
                ).spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                    .pathParam("id", book.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .extract()
                    .body()
                .as(BookDTO.class, objectMapper);

        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertNotNull(createdBook.getId());
        assertNotNull(book.getId());
        assertEquals("Docker Deep Dive - Updated", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(55.99, book.getPrice());
    }

    @Test
    @Order(4)
    void deleteTest() throws JsonProcessingException {

        given(specification)
                .pathParam("id", book.getId())
            .when()
                .delete("{id}")
            .then()
                .statusCode(204);
    }


    @Test
    @Order(5)
    void findAllTest() throws JsonProcessingException {

        var response = given(specification)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .queryParams("page", 9 , "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .extract()
                .body()
                .as(PagedModelBook.class, objectMapper);

        List<BookDTO> content = response.getContent();

        BookDTO bookOne = content.get(0);

        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getTitle());
        assertNotNull(bookOne.getAuthor());
        assertNotNull(bookOne.getPrice());
        assertTrue(bookOne.getId() > 0);
        assertEquals("The Toyota Way", bookOne.getTitle());
        assertEquals("Jeffrey Liker", bookOne.getAuthor());
        assertEquals(138.96, bookOne.getPrice());

        BookDTO foundBookSeven = content.get(7);

        assertNotNull(foundBookSeven.getId());
        assertNotNull(foundBookSeven.getTitle());
        assertNotNull(foundBookSeven.getAuthor());
        assertNotNull(foundBookSeven.getPrice());
        assertTrue(foundBookSeven.getId() > 0);
        assertEquals("The Toyota Way", foundBookSeven.getTitle());
        assertEquals("Jeffrey Liker", foundBookSeven.getAuthor());
        assertEquals(37.66, foundBookSeven.getPrice());
    }

    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(Double.valueOf(55.99));
        book.setLaunchDate(new Date());
    }
}