package com.DoAn.ShoeShop.dto.respone;

import com.DoAn.ShoeShop.entity.Category;
import com.DoAn.ShoeShop.entity.Color;
import com.DoAn.ShoeShop.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Integer quantity;
    private String image;
    private int totalRate;
    private int ratedTimes;
    private int soldQuantity;
    private Category category;
    private List<Color> colors;
    private List<Size> sizes;
    private LocalDateTime createAt;

    public String formatPrice(){
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(this.price);
    }
}
