package mr.iscae.services;

import jakarta.persistence.EntityNotFoundException;
import mr.iscae.constants.Category;
import mr.iscae.dtos.requests.ProduitRequest;
import mr.iscae.dtos.responses.ProduitResponse;
import mr.iscae.entities.Produit;
import mr.iscae.repositories.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final FileService fileService;

    public ProduitService(ProduitRepository produitRepository, FileService fileService) {
        this.produitRepository = produitRepository;
        this.fileService = fileService;
    }


    public ProduitResponse createProduit(ProduitRequest produitRequest, MultipartFile imageFile) throws IOException {
        String imageUrl = fileService.uploadFile(imageFile);
        System.out.println("imageUrl"+imageUrl);
        Produit produit = Produit.builder()
                .name(produitRequest.getName())
                .category(produitRequest.getCategory())
                .image(imageUrl)
                .description(produitRequest.getDescription())
                .price(produitRequest.getPrice())
                .stockQuantity(produitRequest.getStockQuantity())
                .build();

        Produit savedProduit = produitRepository.save(produit);

        return mapToResponse(savedProduit);
    }

    public List<ProduitResponse> searchAndFilter(String name, String category, Double minPrice, Double maxPrice) {
        Category categoryEnum = null;
        if (category != null) {
            try {
                categoryEnum = Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category: " + category);
            }
        }
        List<Produit> produits = produitRepository.searchAndFilter(name, categoryEnum, minPrice, maxPrice);
        return produits.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProduitResponse getProduitById(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produit not found with ID: " + id));
        return mapToResponse(produit);
    }

    public ProduitResponse updateProduit(Long id, ProduitRequest produitRequest, MultipartFile imageFile) throws IOException {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException ("Produit not found with ID: " + id));

        if (produitRequest.getName() != null) produit.setName(produitRequest.getName());
        if (produitRequest.getCategory() != null) produit.setCategory(produitRequest.getCategory());
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileService.uploadFile(imageFile);
            produit.setImage(imageUrl);
        }
        if (produitRequest.getDescription() != null) produit.setDescription(produitRequest.getDescription());
        if (produitRequest.getStockQuantity() != null) produit.setStockQuantity(produitRequest.getStockQuantity());

        Produit updatedProduit = produitRepository.save(produit);
        return mapToResponse(updatedProduit);
    }

    public void deleteProduit(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit not found with ID: " + id));
        produitRepository.delete(produit);
    }

    public List<ProduitResponse> getAllProduits() {
        return produitRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProduitResponse mapToResponse(Produit produit) {
        return ProduitResponse.builder()
                .id(produit.getId())
                .name(produit.getName())
                .category(produit.getCategory())
                .image(produit.getImage())
                .description(produit.getDescription())
                .price(produit.getPrice())
                .stockQuantity(produit.getStockQuantity())
                .build();
    }
}
