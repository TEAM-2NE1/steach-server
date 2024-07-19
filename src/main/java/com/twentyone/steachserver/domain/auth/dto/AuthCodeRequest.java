package com.twentyone.steachserver.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthCodeRequest {
    //1이상 몇 미만
    @Min(1)
    @JsonProperty("number_of_code")
    private Integer numberOfCode;
}
