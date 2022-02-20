package com.tw.capability.gtb.demotddspringboot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskService {


    private final TaskRepository taskRepository; // final修饰词必不可少

    public List<Task> findTasks(Boolean completed) {
        final var tasks = taskRepository.findAll();
        if (Objects.isNull(completed)) {
            return tasks;
        }

        if (Boolean.TRUE.equals(completed)) {
            return tasks.stream().filter(Task::isCompleted).collect(Collectors.toList());
        } else {
            return tasks.stream().filter(task -> !task.isCompleted()).collect(Collectors.toList());
        }
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
}
