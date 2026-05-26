package com.app.javaapp.Controllers;

import com.app.javaapp.Config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    private AppProperties appProperties;

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> info = new HashMap<>();
        info.put("app", appProperties.getName());
        info.put("version", appProperties.getVersion());
        info.put("message", "Welcome to Task Manager API");
        info.put("endpoints", "/api/tasks - CRUD operations available");
        return info;
    }
}