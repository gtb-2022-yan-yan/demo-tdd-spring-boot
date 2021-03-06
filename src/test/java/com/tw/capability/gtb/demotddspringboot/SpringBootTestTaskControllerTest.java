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

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureJsonTesters
class SpringBootTestTaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JacksonTester<List<Task>> taskJson;

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

    @Test
    void should_return_to_be_done_tasks_given_completed_is_false() throws IOException {
        // given
        final var toBeDoneTask = new Task("task 01", false);
        taskRepository.save(toBeDoneTask);
        final var completedTask = new Task("task 02", true);
        taskRepository.save(completedTask);

        // when
        final var responseEntity = restTemplate.getForEntity("/tasks?completed=false", String.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        final var fetchTasks = taskJson.parseObject(responseEntity.getBody());
        assertThat(fetchTasks).hasSize(1);
        assertThat(fetchTasks.get(0).getName()).isEqualTo(toBeDoneTask.getName()); // 分开校验，原task不含id
        assertThat(fetchTasks.get(0).getCompleted()).isEqualTo(toBeDoneTask.getCompleted());

    }
    @Test
    void should_return_to_be_done_tasks_given_completed_is_true() throws IOException {
        // given
        final var toBeDoneTask = new Task("task 01", false);
        taskRepository.save(toBeDoneTask);
        final var completedTask = new Task("task 02", true);
        taskRepository.save(completedTask);

        // when
        final var responseEntity = restTemplate.getForEntity("/tasks?completed=true", String.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        final var fetchTasks = taskJson.parseObject(responseEntity.getBody());
        assertThat(fetchTasks).hasSize(1);
        assertThat(fetchTasks.get(0).getName()).isEqualTo(completedTask.getName()); // 分开校验，原task不含id
        assertThat(fetchTasks.get(0).getCompleted()).isEqualTo(completedTask.getCompleted());

    }

    @Test
    void should_return_created_task_when_add_task() {
        // given
        final var task = new Task("task 01", false);

        // when
        final var responseEntity = restTemplate.postForEntity("/tasks", task, Task.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        final var createdTask = responseEntity.getBody();
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getId()).isPositive();
        assertThat(createdTask.getName()).isEqualTo(task.getName());
        assertThat(createdTask.getCompleted()).isEqualTo(task.getCompleted());


    }


    @Test
    void should_return_bad_request_given_completed_is_null_when_add_task() {
        // given
        final var task = new Task("task 01", null);

        // when
        final var responseEntity = restTemplate.postForEntity("/tasks", task, ErrorResult.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).containsSequence("completed: 不能为null"); // 中文设置的原因返回的信息为中文


    }

}
