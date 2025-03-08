package mr.iscae.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mr.iscae.dtos.requests.AuthenticationRequest;
import mr.iscae.dtos.requests.RegisterRequest;
import mr.iscae.dtos.responses.AuthenticationResponse;
import mr.iscae.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }



}

