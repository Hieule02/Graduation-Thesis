package com.DoAn.ShoeShop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private Long id;
    @NotBlank(message = "Bắt buộc!")
    private String province;
    @NotBlank(message = "Bắt buộc!")
    private String district;
    @NotBlank(message = "Bắt buộc!")
    private String commune;

    private String houseNumber;
}
