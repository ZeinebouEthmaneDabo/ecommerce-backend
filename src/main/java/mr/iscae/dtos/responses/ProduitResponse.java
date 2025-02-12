package mr.iscae.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.iscae.constants.Category;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProduitResponse {
    private Long id;
    private String name;
    private Category category;
    private String image;
    private String description;
    private Double price;
    private Integer stockQuantity;
}
