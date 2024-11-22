package ru.itis.orders.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class NumbersServiceClient {
    private final RestTemplate restTemplate;

    @Value("${number-generate-service.url}")
    private String numberServiceUrl;

    public String getOrderNumber() {
        String url = UriComponentsBuilder.fromHttpUrl(numberServiceUrl)
                .path("/numbers")
                .toUriString();

        log.info("Requesting order number from: {}", url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

}
