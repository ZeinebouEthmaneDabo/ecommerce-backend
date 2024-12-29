package mr.iscae.dtos.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.iscae.constants.CATEGORY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProduitRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Category cannot be null")
    private CATEGORY category;

    @NotBlank(message = "Image cannot be blank")
    @Pattern(
            regexp = "^(http://|https://).*",
            message = "Image must be a valid link starting with http:// or https://"
    )
    private String image;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}

