package com.twentyone.steachserver.domain.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twentyone.steachserver.domain.member.model.Student;

public interface GPTService {
    String getChatGPTResponse(Student student) throws JsonProcessingException, Exception;
}
