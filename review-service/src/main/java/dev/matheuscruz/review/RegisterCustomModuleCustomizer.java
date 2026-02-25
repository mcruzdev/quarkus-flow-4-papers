package dev.matheuscruz.review;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.jackson.JsonFormat;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

@Singleton
public class RegisterCustomModuleCustomizer implements ObjectMapperCustomizer {
    public void customize(ObjectMapper mapper) {
        mapper.registerModule(JsonFormat.getCloudEventJacksonModule());
    }
}

