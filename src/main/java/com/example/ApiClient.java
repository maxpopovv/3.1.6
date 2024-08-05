package com.example;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

public class ApiClient {
    private static final String BASE_URL = "http://94.198.50.185:7081/api/users";
    private RestTemplate restTemplate;
    private HttpHeaders headers;

    public ApiClient() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public void performOperations() {
        String part1 = "";
        String part2 = "";
        String part3 = "";

        try {
            // 1. Get all users
            ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Список пользователей получен успешно.");
                String sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
                headers.set(HttpHeaders.COOKIE, sessionId);
                System.out.println("Session ID: " + sessionId);
            } else {
                System.err.println("Ошибка при получении списка пользователей: " + response.getStatusCode());
                return;
            }

            // 2. Add user
            User newUser = new User();
            newUser.setId(3L);
            newUser.setName("Andryha");
            newUser.setLastName("Volk");
            newUser.setAge((byte) 25);
            response = restTemplate.exchange(BASE_URL, HttpMethod.POST, new HttpEntity<>(newUser, headers), String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Пользователь добавлен успешно.");
                part1 = response.getBody();
                System.out.println("Part 1: " + part1);
            } else {
                System.err.println("Ошибка при добавлении пользователя: " + response.getStatusCode());
                return;
            }

            // 3. Update user
            newUser.setName("Victor");
            newUser.setLastName("naRaene");
            response = restTemplate.exchange(BASE_URL, HttpMethod.PUT, new HttpEntity<>(newUser, headers), String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Пользователь изменен успешно.");
                part2 = response.getBody();
                System.out.println("Part 2: " + part2);
            } else {
                System.err.println("Ошибка при изменении пользователя: " + response.getStatusCode());
                return;
            }

            // 4. Delete user
            response = restTemplate.exchange(BASE_URL + "/3", HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Пользователь удален успешно.");
                part3 = response.getBody();
                System.out.println("Part 3: " + part3);
            } else {
                System.err.println("Ошибка при удалении пользователя: " + response.getStatusCode());
                return;
            }

        } catch (HttpClientErrorException e) {
            System.err.println("HTTP ошибка: " + e.getStatusCode());
        }

        // Concatenate final code
        String finalCode = part1 + part2 + part3;
        System.out.println("Итоговый код: " + finalCode);
    }

    public static void main(String[] args) {
        new ApiClient().performOperations();
    }
}