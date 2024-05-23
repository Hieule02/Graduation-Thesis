package com.DoAn.ShoeShop.dto.request;

import com.DoAn.ShoeShop.entity.Category;
import com.DoAn.ShoeShop.entity.Color;
import com.DoAn.ShoeShop.entity.Size;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long id;
    @NotBlank(message = "Bắt buộc!")
    private String name;
    private String description;
    @NotNull(message = "Bắt buộc!")
    @Positive(message = "Giá tiền phải lớn hơn 0!")
    private Long price;
    @NotNull(message = "Bắt buộc!")
    @PositiveOrZero(message = "Số lượng phải lớn hơn hoặc bằng 0!")
    private Integer quantity;
    private String image;
    private Category category;
    @NotEmpty(message = "Hãy chọn màu sắc!")
    private List<Color> colors;
    @NotEmpty(message = "Hãy chọn kích cỡ!")
    private List<Size> sizes;
}
