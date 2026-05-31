package com.app.javaapp.Services;

import com.app.javaapp.Models.Task;
import com.app.javaapp.Repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TaskServicesTest {
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServices taskServices;

    public TaskServicesTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void createTask() {
    }

    @Test
    void getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("New Task 1", "Task 1 Description"));
        tasks.add(new Task("New Task 2", "Task 2 Description"));

        OngoingStubbing<List<Task>> listOngoingStubbing = when(taskRepository.findAll()).thenReturn(Optional.of(tasks));
    }

    @Test
    void getTaskById() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void getTasksByStatus() {
    }
}