package com.app.javaapp.Services;

import com.app.javaapp.Models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskServices {
    // In-memory storage (for demo purposes)
    private final ConcurrentHashMap<Long, Task> taskStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Create a new task
    public Task createTask(Task task) {
        Long newId = idGenerator.getAndIncrement();
        task.setId(newId);
        taskStore.put(newId, task);
        return task;
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskStore.values());
    }

    // Get a single task by ID
    public Task getTaskById(Long id) {
        Task task = taskStore.get(id);
        if (task == null) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        return task;
    }

    // Update an existing task
    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = taskStore.get(id);
        if (existingTask == null) {
            throw new RuntimeException("Task not found with id: " + id);
        }

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());
        existingTask.setUpdatedAt(java.time.LocalDateTime.now());

        return existingTask;
    }

    // Delete a task
    public void deleteTask(Long id) {
        Task removed = taskStore.remove(id);
        if (removed == null) {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    // Get only completed or pending tasks
    public List<Task> getTasksByStatus(boolean completed) {
        return taskStore.values().stream()
                .filter(task -> task.isCompleted() == completed)
                .toList();
    }
}
