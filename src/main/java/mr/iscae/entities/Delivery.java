package mr.iscae.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.iscae.constants.Status;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deliveredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @ManyToMany
    @JoinTable(
            name = "delivery_products",
            joinColumns = @JoinColumn(name = "delivery_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Produit> products;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;

    @Column(nullable = false)
    private String address;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
