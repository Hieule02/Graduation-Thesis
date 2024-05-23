package com.DoAn.ShoeShop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    private Long id;
    @NotBlank(message = "Bắt buộc!")
    private String name;
}
