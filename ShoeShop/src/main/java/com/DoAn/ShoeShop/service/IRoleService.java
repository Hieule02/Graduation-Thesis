package com.DoAn.ShoeShop.service;

import com.DoAn.ShoeShop.dto.request.RoleRequest;
import com.DoAn.ShoeShop.dto.respone.RoleResponse;
import com.DoAn.ShoeShop.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IRoleService {

    List<RoleResponse> getAll();

    RoleResponse findById(Long id);

    RoleResponse findByName(String name);

    Role createRole(RoleRequest roleRequest);

    void updateRole(RoleRequest roleRequest);

    void deleteById(Long id);
}
