package com.twentyone.steachserver.domain.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface GPTService {
    String getChatGPTResponse(String gptMessage) throws JsonProcessingException, Exception;
}
