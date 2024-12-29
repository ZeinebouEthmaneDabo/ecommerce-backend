package mr.iscae.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.iscae.constants.CATEGORY;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private CATEGORY category;

    @Column(nullable = false, length = 600)
    private String image;

    @Column(nullable = false)
    private Double price;

    @Column(length = 1000)
    private String description;
}
