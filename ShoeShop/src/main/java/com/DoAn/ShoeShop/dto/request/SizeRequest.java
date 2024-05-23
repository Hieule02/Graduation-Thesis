package com.DoAn.ShoeShop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SizeRequest {
    private Long id;
    @Positive(message = "Kích cỡ phải lớn hơn 0!")
    @NotNull(message = "Bắt buộc!")
    private Integer size;
}
