package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class Main {

    public static final String REMOTE_SERVICE_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // Maximum connection time
                        .setSocketTimeout(30000)    // Maximum time to wait for data
                        .setRedirectsEnabled(false)
                        .build())
                .build()) {

            HttpGet request = new HttpGet(REMOTE_SERVICE_URI);

            try (CloseableHttpResponse response = httpClient.execute(request)) {

                List<CatFact> facts = mapper.readValue(
                        response.getEntity().getContent(),
                        new TypeReference<List<CatFact>>() {}
                );

                facts.stream()
                        .filter(fact -> fact.getUpvotes() != null && fact.getUpvotes() > 0)
                        .forEach(System.out::println);

            } catch (IOException e) {
                System.err.println("Ошибка при чтении ответа: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Ошибка при создании или выполнении запроса: " + e.getMessage());
        }
    }
}