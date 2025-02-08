package mr.iscae.repositories;

import mr.iscae.constants.Category;
import mr.iscae.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    @Query("SELECT p FROM Produit p WHERE (:name IS NULL OR p.name ILIKE CONCAT('%', :name, '%')) " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Produit> searchAndFilter(
            @Param("name") String name,
            @Param("category") Category category,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
