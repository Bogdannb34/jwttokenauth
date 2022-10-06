package com.practice.jwttokenauth.controller.user;

import com.practice.jwttokenauth.models.user.User;
import com.practice.jwttokenauth.services.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Getter
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity.ok().body(getUserService().getUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") final Long id) {
        getUserService().deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
