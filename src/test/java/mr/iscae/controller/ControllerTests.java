package mr.iscae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mr.iscae.controllers.AuthenticationController;
import mr.iscae.controllers.OrderController;
import mr.iscae.controllers.ProduitController;
import mr.iscae.dtos.requests.AuthenticationRequest;
import mr.iscae.dtos.requests.RegisterRequest;
import mr.iscae.dtos.responses.AuthenticationResponse;
import mr.iscae.services.AuthenticationService;
import mr.iscae.services.OrderService;
import mr.iscae.services.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ControllerTests {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private ProduitService produitService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private OrderController orderController;

    @InjectMocks
    private ProduitController produitController;

    @InjectMocks
    private AuthenticationController authenticationController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController, produitController, authenticationController).build();
    }

    @Test
    public void testGetAllOrders() throws Exception {
        when(orderService.getAllOrders(null, null)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

//    @Test
//    public void testGetAllProduits() throws Exception {
//        when(produitService.getAllProduits()).thenReturn(Collections.emptyList());
//        mockMvc.perform(get("/api/produits"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }

    @Test
    public void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("testuser");
        request.setPassword("password123");
        request.setEmail("testuser@example.com");
        request.setPhone("42211414");

      //  when(authenticationService.register(any(RegisterRequest.class))).thenReturn("Success");
    
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    
//    @Test
//    public void testAuthenticate() throws Exception {
//        AuthenticationRequest request = new AuthenticationRequest();
//        request.setEmail("testuser@example.com");
//        request.setPassword("password123");
//
//     //   AuthenticationResponse response = new AuthenticationResponse("fake-token");
//
//        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);
//
//        mockMvc.perform(post("/api/auth/authenticate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.access_token").value("fake-token"));
//    }

    
}

