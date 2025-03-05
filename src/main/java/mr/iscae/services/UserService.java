package mr.iscae.services;

import lombok.RequiredArgsConstructor;
import mr.iscae.constants.Role;
import mr.iscae.dtos.responses.UserResponse;
import mr.iscae.entities.User;
import mr.iscae.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> getAllNonAdminUsers() {
        List<User> users = userRepository.findByRoleNot(Role.ADMIN);
        return users.stream()
                .map(user -> new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getPhone()))
                .collect(Collectors.toList());
    }
}
