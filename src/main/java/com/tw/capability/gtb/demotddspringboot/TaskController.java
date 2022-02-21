package com.tw.capability.gtb.demotddspringboot;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Task> fetchTasks(@RequestParam(required = false) Boolean completed) { // Boolean是对象，可以为空
        return taskService.findTasks(completed);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 最好显示声明，否则默认200=OK
    public Task createTask(@Valid @RequestBody Task task) { // 参数校验
        return taskService.createTask(task);
    }

}
