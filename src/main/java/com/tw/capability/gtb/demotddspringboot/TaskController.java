package com.tw.capability.gtb.demotddspringboot;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {


    private final TaskRepository taskRepository;

    @GetMapping
    public List<Task> fetchTasks(@RequestParam(required = false) Boolean completed) { // Boolean是对象，可以为空
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

}
