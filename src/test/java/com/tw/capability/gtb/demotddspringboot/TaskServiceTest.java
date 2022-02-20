package com.tw.capability.gtb.demotddspringboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void should_return_empty_tasks() {
        // given
        when(taskRepository.findAll()).thenReturn(emptyList());

        // when
        final var tasks = taskService.findTasks(null);

        // then
        assertThat(tasks).isEmpty();
    }

    @Test
    void should_return_multiple_tasks() {
        // given
        final var tasks = List.of(
                new Task(1L, "task 01", false),
                new Task(2L, "task 02", true)
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        // when
        final var foundTasks = taskService.findTasks(null);

        // then
        assertThat(foundTasks).isEqualTo(tasks);
        verify(taskRepository).findAll();
    }

    @Test
    void should_return_to_be_done_tasks_when_given_completed_is_false() {
        // given
        final var tasks = List.of(
                new Task(1L, "task 01", false),
                new Task(2L, "task 02", true)
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        // when
        final var foundTasks = taskService.findTasks(false);

        // then
        assertThat(foundTasks).isEqualTo(List.of(tasks.get(0)));
        verify(taskRepository).findAll();
    }

    @Test
    void should_return_to_be_done_tasks_when_given_completed_is_true() {
        // given
        final var tasks = List.of(
                new Task(1L, "task 01", false),
                new Task(2L, "task 02", true)
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        // when
        final var foundTasks = taskService.findTasks(true);

        // then
        assertThat(foundTasks).isEqualTo(List.of(tasks.get(1)));
        verify(taskRepository).findAll(); // 方法是否被真正调用
    }

    @Test
    void should_return_created_task_when_create_task() {
        // given
        final var task = new Task("task 01", false);
        final var savedTask = new Task(1L, "task 01", false);

        when(taskRepository.save(task)).thenReturn(savedTask);

        // when
        final var createdTask = taskService.createTask(task);

        // then
        assertThat(createdTask).isEqualTo(savedTask);
        verify(taskRepository).save(task);
    }

}
