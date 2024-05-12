package ru.spring.crudtask313.springcrudtask.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "apiproperties")
public class ApiProperties {
    private String geoCoderUrl;
    private String geoCoderApikey;
    private String weatherUrl;
    private String weatherApikey;
}
