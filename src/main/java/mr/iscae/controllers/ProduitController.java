package mr.iscae.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import mr.iscae.dtos.requests.ProduitRequest;
import mr.iscae.dtos.responses.ProduitResponse;
import mr.iscae.services.FileService;
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
            @Valid @RequestPart("request") ProduitRequest request,
            @RequestPart(value = "image") MultipartFile image
    ) {
        try {
            System.out.println("Received request: " + request);
            System.out.println("Image: " + (image != null ? image.getOriginalFilename() : "No image uploaded"));

                if (!FileService.isValidImage(image)) {
                    throw new IllegalArgumentException("Invalid image type.");
                }


            ProduitResponse response = produitService.createProduit(request, image);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProduitResponse>> getAllProduits() {
        List<ProduitResponse> produits = produitService.getAllProduits();
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProduitResponse>> searchAndFilterProduits(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        List<ProduitResponse> produits = produitService.searchAndFilter(name, category, minPrice, maxPrice);
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
            @Valid @RequestPart("request") ProduitRequest request,
            @RequestPart(value = "image") MultipartFile imageFile
    ) {
        try {
            System.out.println("Received request: " + request);
            System.out.println("Image: " + (imageFile != null ? imageFile.getOriginalFilename() : "No image uploaded"));

            if (!FileService.isValidImage(imageFile)) {
                throw new IllegalArgumentException("Invalid image type.");
            }

            ProduitResponse response = produitService.updateProduit(id, request, imageFile);
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
