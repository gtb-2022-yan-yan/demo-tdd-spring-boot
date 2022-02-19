package com.tw.capability.gtb.demotddspringboot;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private boolean completed;

    public Task(String name, boolean completed) {
        // 创建task时不知道id
        this.name = name;
        this.completed = completed;
    }
}
