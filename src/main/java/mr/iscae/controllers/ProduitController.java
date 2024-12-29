package mr.iscae.controllers;

import jakarta.validation.Valid;
import mr.iscae.dtos.requests.ProduitRequest;
import mr.iscae.dtos.responses.ProduitResponse;
import mr.iscae.services.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProduitResponse> createProduit(
            @ModelAttribute ProduitRequest produitRequest,
            @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            ProduitResponse response = produitService.createProduit(produitRequest, imageFile);
            return ResponseEntity.ok(response);
        } catch ( IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProduitResponse>> getAllProduits() {
        List<ProduitResponse> produits = produitService.getAllProduits();
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProduitResponse> getProduitById(@PathVariable Long id) {
        ProduitResponse response = produitService.getProduitById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProduitResponse> updateProduit(
            @PathVariable Long id,
            @ModelAttribute ProduitRequest produitRequest,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            ProduitResponse response = produitService.updateProduit(id, produitRequest, imageFile);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }
}
