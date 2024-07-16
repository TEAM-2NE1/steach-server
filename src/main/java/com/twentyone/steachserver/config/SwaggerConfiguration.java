package com.twentyone.steachserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "스티치 백엔드 API 명세서",
                description = """
                        스티치 백엔드 API 명세입니다.<br>
                        <h2>401: 만료된 토큰 or 유효하지 않은 토큰 사용</h2>
                        <h2>404: 잘못된 입력</h2>
                        <h2>500: 서버에러</h2>
                        """,
                version = "v1"
        ),
        servers = {
                @Server(url = "/", description = "API 서버")
        }
)
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openAPI() {
        //jwt 토큰 자동추가 설정
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement schemaRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(schemaRequirement));
    }
}
