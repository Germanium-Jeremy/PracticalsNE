package com.app.javaapp.Services;

import com.app.javaapp.Models.Task;
import com.app.javaapp.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServices {

    @Autowired
    private TaskRepository taskRepository;

    // Create a new task
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get a single task by ID
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task with id " + id + "is not found!"));
    }

    // Update an existing task
    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = getTaskById(id);
        if (existingTask == null) {
            throw new RuntimeException("Task not found with id: " + id);
        }

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());
        existingTask.setUpdatedAt(java.time.LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    // Delete a task
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        if (task == null) {
            throw new RuntimeException("Task with id " + id + "not found");
        }
        taskRepository.delete(task);
    }

    // Get only completed or pending tasks
    public List<Task> getTasksByStatus(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }
}
