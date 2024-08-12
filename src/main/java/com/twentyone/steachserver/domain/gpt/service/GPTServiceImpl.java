package com.twentyone.steachserver.domain.gpt.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GPTServiceImpl implements GPTService {

    @Value("${api.gpt.key}")
    private String apiKey;

    @Override
    public String getChatGPTResponse(String gptMessage) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Constructing the JSON payload using ObjectMapper
            Map<String, Object> message = Map.of("role", "user", "content", gptMessage);
            Map<String, Object> payload = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", List.of(message),
                    "max_tokens", 100
            );
            String requestBody = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            // Parse the response to extract the "content" field
            JsonNode jsonNode = objectMapper.readTree(response.body());
            String content = jsonNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            return content;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error occurred while getting response from OpenAI.";
        }
    }
}

//       System.out.println(response);
//            System.out.println(response.body());
//        System.out.println(Arrays.toString(response.body().getBytes(StandardCharsets.UTF_8)));
//ObjectMapper objectMapper = new ObjectMapper();
//Map<String, Object> map = objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
//Object choices = map.get("choices");
//            System.out.println(choices);


// JSON 파싱
//ObjectMapper objectMapper = new ObjectMapper();
//JsonNode jsonNode = objectMapper.readTree(response.body());
//
//// "content" 필드 추출
//String content = jsonNode
//        .path("choices")
//        .get(0)  // 첫 번째 요소 선택
//        .path("message")
//        .path("content")
//        .asText();

//            System.out.println(content);


//@Override
//public String getChatGPTResponse(String gptMessage) {
//    HttpClient client = HttpClient.newHttpClient();
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    try {
//        // Constructing the JSON payload using ObjectMapper
//        Map<String, Object> message = Map.of("role", "user", "content", gptMessage);
//        Map<String, Object> payload = Map.of(
//                "model", "gpt-3.5-turbo",
//                "messages", List.of(message),
//                "max_tokens", 100
//        );
//        String requestBody = objectMapper.writeValueAsString(payload);
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
//                .header("Content-Type", "application/json")
//                .header("Authorization", "Bearer " + apiKey)
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
//
//        // Parse the response to extract the "content" field
//        JsonNode jsonNode = objectMapper.readTree(response.body());
//        String content = jsonNode
//                .path("choices")
//                .get(0)
//                .path("message")
//                .path("content")
//                .asText();
//
//        return content;
//
//    } catch (IOException | InterruptedException e) {
//        e.printStackTrace();
//        return "Error occurred while getting response from OpenAI.";
//    }
//}