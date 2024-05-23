package com.DoAn.ShoeShop.dto.respone;

import com.DoAn.ShoeShop.entity.Address;
import com.DoAn.ShoeShop.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String phone;
    private String username;
    private String password;
    private List<Address> addresses;
    private List<Role> roles;
}
