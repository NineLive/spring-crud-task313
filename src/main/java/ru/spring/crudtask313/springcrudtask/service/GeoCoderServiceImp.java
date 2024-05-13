package ru.spring.crudtask313.springcrudtask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.spring.crudtask313.springcrudtask.property.GeoCoderProperties;
import ru.spring.crudtask313.springcrudtask.util.Coordinates;

@Service
public class GeoCoderServiceImp implements GeoCoderService {
    private final GeoCoderProperties geoCoderProperties;

    @Autowired
    public GeoCoderServiceImp(GeoCoderProperties geoCoderProperties) {
        this.geoCoderProperties = geoCoderProperties;
    }

    public Coordinates getCoordinatesByAddress(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String url = geoCoderProperties.getUrl();
        String apiKey = geoCoderProperties.getApikey();
        String urlWithParameters = String.format("%s?apikey=%s&geocode=%s&lang=%s&format=%s&results=%s", url, apiKey, address, "ru_RU", "json", 1);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(urlWithParameters, String.class);
        return parseCoordinatesFromJson(responseEntity.getBody());
    }

    private static Coordinates parseCoordinatesFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            String coordinates = root.at("/response/GeoObjectCollection/featureMember/0/GeoObject/Point/pos").asText();
//            Coordinates coordinatesObj = mapper.readValue(coordinates, Coordinates.class);
            return Coordinates.builder()
                    .longitude(coordinates.split(" ")[0])
                    .latitude(coordinates.split(" ")[1])
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
