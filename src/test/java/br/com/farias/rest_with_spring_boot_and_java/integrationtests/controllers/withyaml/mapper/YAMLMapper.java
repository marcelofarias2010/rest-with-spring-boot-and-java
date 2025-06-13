package br.com.farias.rest_with_spring_boot_and_java.integrationtests.controllers.withyaml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper; // Importação correta do Jackson
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

// A classe deve implementar a interface do RestAssured
public class YAMLMapper implements io.restassured.mapper.ObjectMapper {

    private final ObjectMapper objectMapper;
    private final TypeFactory typeFactory;

    public YAMLMapper() {
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        typeFactory = TypeFactory.defaultInstance();
    }

    // Este é o método que faltava, para acessarmos o mapper interno
    public ObjectMapper getMapper() {
        return objectMapper;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object deserialize(ObjectMapperDeserializationContext context) {
        try {
            String dataToDeserialize = context.getDataToDeserialize().asString();
            Class type = (Class) context.getType();
            return objectMapper.readValue(dataToDeserialize, typeFactory.constructType(type));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Erro ao deserializar o conteúdo YAML", e);
        }
    }

    @Override
    public Object serialize(ObjectMapperSerializationContext context) {
        try {
            return objectMapper.writeValueAsString(context.getObjectToSerialize());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Erro ao serializar para YAML", e);
        }
    }
}
