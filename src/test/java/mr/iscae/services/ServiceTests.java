package mr.iscae.services;

import mr.iscae.constants.Role;
import mr.iscae.dtos.requests.AuthenticationRequest;
import mr.iscae.dtos.requests.RegisterRequest;
import mr.iscae.dtos.responses.AuthenticationResponse;
import mr.iscae.entities.User;
import mr.iscae.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Test User");
        request.setEmail("testuser@example.com");
        request.setPassword("password123");
        request.setPhone("1234567890");

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String response = authenticationService.register(request);
        assertEquals("User registered successfully!", response);
    }

    @Test
    public void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("testuser@example.com");
        request.setPassword("password123");

        User user = User.builder()
                .email("testuser@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("fake-jwt-token");

        AuthenticationResponse response = authenticationService.authenticate(request);
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getAccessToken());
    }
}
