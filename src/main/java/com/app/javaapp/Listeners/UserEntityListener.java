package com.app.javaapp.Listeners;

import com.app.javaapp.Models.Task;
import com.app.javaapp.Models.User;
import com.app.javaapp.Services.TaskServices;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEntityListener {

    private static TaskServices taskService;

    @Autowired
    public void init(TaskServices service) {
        UserEntityListener.taskService = service;
    }

    @PostPersist
    public void createDefaultTaskForUser(User user) {
        // Create a default task associated with the new user
        // Note: If your Task entity currently doesn't have a user relationship,
        // you'll need to add a ManyToOne field in Task (see "Linking Tasks to Users" below)
        Task defaultTask = new Task(
                "Welcome!",
                "Thanks for joining " + user.getUsername() + ". This is your first task."
        );
        defaultTask.setUser(user);  // you need to add this field
        taskService.createTask(defaultTask);
        System.out.println("Default task created for user: " + user.getUsername());
    }
}