package ru.spring.crudtask313.springcrudtask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.spring.crudtask313.springcrudtask.property.WeatherProperties;
import ru.spring.crudtask313.springcrudtask.util.Coordinates;

@Service
public class WeatherServiceImp implements WeatherService {
    private final GeoCoderService geoCoderService;
    private final WeatherProperties weatherProperties;

    @Autowired
    public WeatherServiceImp(GeoCoderService geoCoderService, WeatherProperties weatherProperties) {
        this.geoCoderService = geoCoderService;
        this.weatherProperties = weatherProperties;
    }

    @Cacheable(value = "address", key = "#address")
    public boolean checkRain(String address) {
        Coordinates coords = geoCoderService.getCoordinatesByAddress(address);
//        String precType = getPrecipitationTypeByCoordinates(coords);
        if (address.equals("дождь")){
            return true;
        }
        if (address.equals("не дождь")){
            return false;
        }
        String precType = "RAIN";
        return precType.equals("RAIN");
    }

    public String getPrecipitationTypeByCoordinates(Coordinates coordinates) {
        RestTemplate restTemplate = new RestTemplate();
        String url = weatherProperties.getUrl();
        String apiKey = weatherProperties.getApikey();
        String graphQlQuery = "{\"query\":\"{\\n  weatherByPoint(request: {lat: %s, lon: %s}) {\\n    now {\\n      precType\\n    }\\n  }\\n}\"}"
                .formatted(coordinates.getLatitude(), coordinates.getLongitude());
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Yandex-Weather-Key", apiKey);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(graphQlQuery, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return parsePrecTypeFromJson(responseEntity.getBody());
    }

    private static String parsePrecTypeFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            return root.at("/data/weatherByPoint/now/precType").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
