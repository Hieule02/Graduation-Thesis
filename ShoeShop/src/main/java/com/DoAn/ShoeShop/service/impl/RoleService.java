package com.DoAn.ShoeShop.service.impl;

import com.DoAn.ShoeShop.dto.request.RoleRequest;
import com.DoAn.ShoeShop.dto.respone.RoleResponse;
import com.DoAn.ShoeShop.entity.Role;
import com.DoAn.ShoeShop.repository.RoleRepository;
import com.DoAn.ShoeShop.service.IRoleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleService implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<RoleResponse> getAll() {
        return modelMapper.map(roleRepository.findAll(), new TypeToken<List<RoleResponse>>() {}.getType()) ;
    }

    @Override
    public RoleResponse findById(Long id) {
        return modelMapper.map(roleRepository.findById(id), RoleResponse.class);
    }

    @Override
    public RoleResponse findByName(String name) {
        Role role = roleRepository.findByName(name);
        if(role == null){
            return null;
        }
        return modelMapper.map(role, RoleResponse.class);
    }

    @Override
    public Role createRole(RoleRequest roleRequest) {
        Role role = new Role();
        role.setName(roleRequest.getName());
        return roleRepository.save(role);
    }

    @Override
    public void updateRole(RoleRequest roleRequest) {
        Role role = modelMapper.map(roleRequest, Role.class);
        roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }


}
