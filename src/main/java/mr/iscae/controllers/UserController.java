package mr.iscae.controllers;

import lombok.RequiredArgsConstructor;
import mr.iscae.dtos.responses.UserResponse;
import mr.iscae.entities.User;
import mr.iscae.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/non-admins")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getNonAdminUsers() {
        return userService.getAllNonAdminUsers();
    }
}
