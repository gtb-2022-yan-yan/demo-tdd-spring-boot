package com.tw.capability.gtb.demotddspringboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootTestTaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void should_return_empty_tasks() {
        // given

        // when
        final var responseBody = restTemplate.getForEntity("/tasks", List.class);

        // then
        assertThat(responseBody.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseBody.getBody()).isEmpty();
    }
}
