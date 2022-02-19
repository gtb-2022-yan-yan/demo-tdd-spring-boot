package com.tw.capability.gtb.demotddspringboot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureJsonTesters
class SpringBootTestTaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JacksonTester<List<Task>> taskJson;

    @Autowired
    private TaskController taskController;

    @Autowired
    private TaskRepository taskRepository;

    @AfterEach
    void tearDown() {
        taskRepository.deleteAll(); // 数据清理 - 每次测试数据之间不影响
    }

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

    @Test
    void should_return_multiple_tasks() throws IOException {
        // given
        final var tasks = List.of(
                new Task("task 01", true),
                new Task("task 02", false));

        taskRepository.saveAll(tasks); // for multiple tasks

        // when
        final var responseEntity = restTemplate.getForEntity("/tasks", String.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        final var fetchedTasks = responseEntity.getBody();
        assertThat(taskJson.parseObject(fetchedTasks)).isEqualTo(tasks); // json format
    }
}
