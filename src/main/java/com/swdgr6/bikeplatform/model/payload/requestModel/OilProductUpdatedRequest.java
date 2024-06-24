package com.swdgr6.bikeplatform.model.payload.requestModel;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OilProductUpdatedRequest {
    private String name;
    private String description;
    @Positive(message = "Price should be a positive value!")
    private double price;
    private String techStand;
    @Pattern(regexp = "Synthetic|Conventional|Semi-Synthetic", message = "Oil type must be Synthetic, Conventional, or Semi-Synthetic")
    private String oilType;
    private String imageUrl;
    private MultipartFile file;
    private Long brandId;
}
