package br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.wrapper.xml;


import br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.BookDTO;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.List;

@JacksonXmlRootElement(localName = "BookDTO")
public class PagedModelBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    public List<BookDTO> content;

    public PagedModelBook() {}

    public List<BookDTO> getContent() {
        return content;
    }

    public void setContent(List<BookDTO> content) {
        this.content = content;
    }
}
