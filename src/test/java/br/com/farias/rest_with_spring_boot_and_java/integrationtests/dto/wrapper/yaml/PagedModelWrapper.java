package br.com.farias.rest_with_spring_boot_and_java.integrationtests.dto.wrapper.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;

import java.io.Serializable;
import java.util.List;

// Esta classe wrapper sabe como ler a resposta paginada HATEOAS.
public class PagedModelWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    // A anotação diz ao Jackson para procurar um campo "_embedded" na resposta YAML.
    @JsonProperty("_embedded")
    private PersonEmbedded _embedded;

    public PagedModelWrapper() {}

    public PersonEmbedded get_embedded() {
        return _embedded;
    }

    public void set_embedded(PersonEmbedded _embedded) {
        this._embedded = _embedded;
    }

    // Classe interna para representar o conteúdo de "_embedded"
    public static class PersonEmbedded implements Serializable {

        private static final long serialVersionUID = 1L;

        // O Jackson irá preencher esta lista com o conteúdo de "personDTOList".
        // Este é o nome padrão que o Spring HATEOAS usa para uma lista de PersonDTO.
        @JsonProperty("personDTOList")
        private List<PersonDTO> content;

        public PersonEmbedded() {}

        public List<PersonDTO> getContent() {
            return content;
        }

        public void setContent(List<PersonDTO> content) {
            this.content = content;
        }
    }
}
