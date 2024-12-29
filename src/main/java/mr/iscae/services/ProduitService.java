package mr.iscae.services;

import mr.iscae.dtos.requests.ProduitRequest;
import mr.iscae.dtos.responses.ProduitResponse;
import mr.iscae.entities.Produit;
import mr.iscae.repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;



    public ProduitResponse createProduit(ProduitRequest produitRequest, MultipartFile imageFile) throws IOException {
        // Upload the file and get the file URL
        String imageUrl = FileService.uploadFile(imageFile);

        Produit produit = Produit.builder()
                .name(produitRequest.getName())
                .category(produitRequest.getCategory())
                .image(imageUrl) // Use the uploaded image URL
                .description(produitRequest.getDescription())
                .build();

        Produit savedProduit = produitRepository.save(produit);

        return mapToResponse(savedProduit);
    }

    public List<ProduitResponse> getAllProduits() {
        return produitRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProduitResponse getProduitById(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit not found with ID: " + id));
        return mapToResponse(produit);
    }

    public ProduitResponse updateProduit(Long id, ProduitRequest produitRequest, MultipartFile imageFile) throws IOException {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit not found with ID: " + id));

        if (produitRequest.getName() != null) produit.setName(produitRequest.getName());
        if (produitRequest.getCategory() != null) produit.setCategory(produitRequest.getCategory());
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = FileService.uploadFile(imageFile);
            produit.setImage(imageUrl);
        }
        if (produitRequest.getDescription() != null) produit.setDescription(produitRequest.getDescription());

        Produit updatedProduit = produitRepository.save(produit);
        return mapToResponse(updatedProduit);
    }

    public void deleteProduit(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit not found with ID: " + id));
        produitRepository.delete(produit);
    }

    private ProduitResponse mapToResponse(Produit produit) {
        return ProduitResponse.builder()
                .id(produit.getId())
                .name(produit.getName())
                .category(produit.getCategory())
                .image(produit.getImage())
                .description(produit.getDescription())
                .build();
    }
}
