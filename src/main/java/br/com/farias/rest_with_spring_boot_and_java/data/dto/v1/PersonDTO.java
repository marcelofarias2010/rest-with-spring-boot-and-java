package br.com.farias.rest_with_spring_boot_and_java.data.dto.v1;

import br.com.farias.rest_with_spring_boot_and_java.data.dto.BookDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

//@JsonPropertyOrder({"id","first_name","last_name","address","gender"})
//@JsonFilter("PersonFilter")
@JacksonXmlRootElement(localName = "PersonDTO")
@Relation(collectionRelation = "people")
public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private Boolean enabled;
    private String profileUrl;
    private String photoUrl;
    @JsonIgnore
    private List<BookDTO> books;

    public PersonDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getName() {
        return (firstName != null ? firstName : "") +
                (lastName != null ? " " + lastName : "");
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return Objects.equals(id, personDTO.id) && Objects.equals(firstName, personDTO.firstName) && Objects.equals(lastName, personDTO.lastName) && Objects.equals(address, personDTO.address) && Objects.equals(gender, personDTO.gender) && Objects.equals(enabled, personDTO.enabled) && Objects.equals(profileUrl, personDTO.profileUrl) && Objects.equals(photoUrl, personDTO.photoUrl) && Objects.equals(books, personDTO.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, firstName, lastName, address, gender, enabled, profileUrl, photoUrl, books);
    }
}
