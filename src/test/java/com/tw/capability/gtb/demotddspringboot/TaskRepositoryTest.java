package com.tw.capability.gtb.demotddspringboot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

//框架及数据库 - 集成测试 - 与数据库实际交互
@DataJpaTest // 针对数据库的测试
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

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
        assertThat(foundTasks).hasSize(2);
        // 与数据库交互的集成测试中，多个测试运行先后顺序并不确定，获取到的id（自动增加）也无法保证顺序，所以只测试其余数据
        assertThat(foundTasks.get(0).getName()).isEqualTo("task 01");
        assertThat(foundTasks.get(0).getCompleted()).isTrue();
        assertThat(foundTasks.get(1).getName()).isEqualTo("task 02");
        assertThat(foundTasks.get(1).getCompleted()).isFalse();
    }

    @Test
    void should_return_saved_task_when_save_task() {
        // given
        final var task = new Task("task 01", false);
        entityManager.persist(task);

        // when
        final var savedTask = taskRepository.save(task);

        // then
        assertThat(savedTask).isEqualTo(new Task(1L, "task 01", false));
    }
}
