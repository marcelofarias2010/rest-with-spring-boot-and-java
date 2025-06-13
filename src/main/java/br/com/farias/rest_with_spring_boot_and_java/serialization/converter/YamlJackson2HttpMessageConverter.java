package br.com.farias.rest_with_spring_boot_and_java.serialization.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public final class YamlJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    // CORREÇÃO: O construtor deve ser público para que o Spring possa instanciá-lo.
    public YamlJackson2HttpMessageConverter() {
        super(
                new YAMLMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL),
                // Usando o media type "application/x-yaml" para consistência com o WebConfig.
                MediaType.valueOf("application/x-yaml")
        );
    }
}
