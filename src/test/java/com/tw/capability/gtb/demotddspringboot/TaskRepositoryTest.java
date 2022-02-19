package com.tw.capability.gtb.demotddspringboot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

//框架及数据库
@DataJpaTest // 内存数据库
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    void should_return_empty_list() {
        // given

        // when
        final var tasks = taskRepository.findAll();

        // then
        assertThat(tasks).isEmpty();
    }

    @Test
    void should_return_multiple_tasks() {

        // given
        entityManager.persist(new Task("task 01", true));
        entityManager.persist(new Task("task 02", false));

        // when
        final var foundTasks = taskRepository.findAll();

        // then
        assertThat(foundTasks)
                .hasSize(2)
                .containsOnly(
                        new Task(1L,"task 01", true),
                        new Task(2L, "task 02", false)
                );
    }
}
