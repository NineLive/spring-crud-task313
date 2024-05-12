package ru.spring.crudtask313.springcrudtask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.spring.crudtask313.springcrudtask.config.ApiProperties;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherServiceImp {
    private UserService userService;
    private ApiProperties apiProperties;

    @Autowired
    public WeatherServiceImp(UserService userService , ApiProperties apiProperties) {
        this.userService = userService;
        this.apiProperties = apiProperties;
    }

    public boolean checkRain(String address) {
        Map<String, String> coordsMap = getCoordinates(address);
        String precType = getWeatherByCoordinates(coordsMap);

        //Проверяем есть ли дождь по влажности(влажность лажа, проверям по типу осадков)
        return precType.equals("RAIN");
    }

    public Map<String, String> getCoordinates(String address) {
        //Запрос в geocoder для получения кординат по адресу
        RestTemplate restTemplate = new RestTemplate();
        String url = apiProperties.getGeoCoderUrl();
        String apiKey = apiProperties.getGeoCoderApikey();
        url += "?" + "apikey=" + apiKey;
        url += "&" + "geocode=" + address;
        url += "&" + "lang=ru_RU";
        url += "&" + "format=json";
        url += "&" + "results=1";

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return parseCoordinatesFromJson(responseEntity.getBody());
    }

    private static Map<String, String> parseCoordinatesFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            String coordinates = root.at("/response/GeoObjectCollection/featureMember/0/GeoObject/Point/pos").asText();
            Map<String, String> map = new HashMap<>();
            map.put("longitude", coordinates.split(" ")[0]);
            map.put("latitude", coordinates.split(" ")[1]);
            return map;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getWeatherByCoordinates(Map<String, String> coordinates) {
        //Запрос в яндекс для получения погоды по координатам
        String longitude = coordinates.get("longitude");
        String latitude = coordinates.get("latitude");

        RestTemplate restTemplate = new RestTemplate();
        String url = apiProperties.getWeatherUrl();
        String apiKey = apiProperties.getWeatherApikey();
        String graphQlQuery = "{\"query\":\"{\\n  weatherByPoint(request: {lat: %s, lon: %s}) {\\n    now {\\n      precType\\n    }\\n  }\\n}\"}".formatted(latitude, longitude);

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
