package com.julian.iagentebot.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.julian.iagentebot.model.ChatRequest;


@Component
public class IAgenteClient {

    private final RestClient restClient;

    @Value("${iagente.api.url}")
    private String apiUrl;

    public IAgenteClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public String chat(String userId, String message) {

        ChatRequest request =
                new ChatRequest(userId, message);

        return restClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(String.class);
    }
}