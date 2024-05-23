package com.DoAn.ShoeShop.dto.request;

import com.DoAn.ShoeShop.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {
    private Long id;
    @Email(message = "Bạn phải nhập đúng định dạng email!")
    @NotBlank(message = "Bắt buộc!")
    private String email;
    @NotBlank(message = "Bắt buộc!")
    @Pattern(regexp = "^[0-9]{10,12}$", message = "Số điện thoại phải có 10 - 12 chữ số!")
    private String phone;
    @NotBlank(message = "Bắt buộc!")
    private String username;
    private List<Role> roles;
}
