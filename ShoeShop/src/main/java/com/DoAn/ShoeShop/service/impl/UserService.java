package com.DoAn.ShoeShop.service.impl;

import com.DoAn.ShoeShop.dto.request.AddressRequest;
import com.DoAn.ShoeShop.dto.request.UpdateAccountRequest;
import com.DoAn.ShoeShop.dto.request.UserRequest;
import com.DoAn.ShoeShop.dto.respone.UserResponse;
import com.DoAn.ShoeShop.entity.Address;
import com.DoAn.ShoeShop.entity.User;
import com.DoAn.ShoeShop.repository.AddressRepository;
import com.DoAn.ShoeShop.repository.RoleRepository;
import com.DoAn.ShoeShop.repository.UserRepository;
import com.DoAn.ShoeShop.service.IUserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<UserResponse> getAll() {
        return modelMapper.map(userRepository.findAll(), new TypeToken<List<UserResponse>>() {
        }.getType());
    }

    @Override
    public Page<UserResponse> getAll(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable).map(entity ->
                modelMapper.map(entity, UserResponse.class));
    }

    @Override
    public User save(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        if(userRequest.getRoles().isEmpty()){
            user.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        } else {
            user.setRoles(userRequest.getRoles());
        }
        return userRepository.save(user);
    }

    @Override
    public void updateUser(UpdateAccountRequest userRequest) {
        User user = userRepository.findById(userRequest.getId()).get();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getRoles().isEmpty()){
            user.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        } else {
            user.setRoles(userRequest.getRoles());
        }
        userRepository.save(user);
    }

    @Override
    public UserResponse findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()){
            return null;
        }
        return modelMapper.map(user.get(), UserResponse.class);
    }

    @Override
    public UserResponse findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()){
            return null;
        }
        return modelMapper.map(user.get(), UserResponse.class);
    }

    @Override
    public void addAddress(AddressRequest addressRequest, Long id) {
        User user = userRepository.findById(id).get();
        Address address = modelMapper.map(addressRequest, Address.class);
        address.setUser(user);
        addressRepository.save(address);
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id).get();
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public int countCustomer() {
        return userRepository.countCustomer();
    }
}
