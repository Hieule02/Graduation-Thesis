package com.DoAn.ShoeShop.dto.respone;


import com.DoAn.ShoeShop.entity.OrderDetail;
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
public class OrderResponse {
    private Long id;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String paymentMethod;
    private Long productAmount;
    private Long shippingFee;
    private List<OrderDetail> orderDetails;
    private LocalDateTime createAt;
    private String status;
    public String formatPrice(Long money){
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(money);
    }
}
