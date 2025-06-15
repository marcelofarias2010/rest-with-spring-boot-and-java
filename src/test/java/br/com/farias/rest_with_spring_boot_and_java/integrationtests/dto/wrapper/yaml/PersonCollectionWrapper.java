package br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.wrapper.yaml;

import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

// Este wrapper simples mapeia a resposta que contém uma lista "content".
public class PersonCollectionWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    // A anotação @JsonProperty diz ao Jackson para procurar um campo "content" no YAML.
    @JsonProperty("content")
    private List<PersonDTO> content;

    public PersonCollectionWrapper() {}

    public List<PersonDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonDTO> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonCollectionWrapper that = (PersonCollectionWrapper) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}


