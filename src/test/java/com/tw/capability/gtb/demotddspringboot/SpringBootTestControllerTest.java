package com.tw.capability.gtb.demotddspringboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootTestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void should_return_hello_world(){
        // given

        // when
        final var responseBody = restTemplate.getForEntity("/hello", String.class);

        // then
        assertThat(responseBody.getBody()).isEqualTo("Hello world");
    }
}
