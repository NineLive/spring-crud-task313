package ru.spring.crudtask313.springcrudtask.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "geocoder")
public class GeoCoderProperties {
    private String url;
    private String apikey;
}
