package com.concerto.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.concerto.crud.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUsers")
    public void addUsers(@RequestBody List<Map<String, Object>> usersList) {
        userService.addUsers(usersList);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userMap) {
        userService.updateUser(id, userMap);
    }
}
