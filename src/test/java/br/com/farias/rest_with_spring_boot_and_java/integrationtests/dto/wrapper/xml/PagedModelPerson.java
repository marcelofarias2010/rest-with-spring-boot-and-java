package br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.wrapper.xml;

import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.List;

@JacksonXmlRootElement(localName = "PersonDTO")
public class PagedModelPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    public List<PersonDTO> content;

    public PagedModelPerson() {}

    public List<PersonDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonDTO> content) {
        this.content = content;
    }
}
