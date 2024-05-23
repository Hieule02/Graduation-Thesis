package com.DoAn.ShoeShop.service;

import com.DoAn.ShoeShop.dto.request.AddressRequest;
import com.DoAn.ShoeShop.dto.request.UpdateAccountRequest;
import com.DoAn.ShoeShop.dto.request.UserRequest;
import com.DoAn.ShoeShop.dto.respone.UserResponse;
import com.DoAn.ShoeShop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    List<UserResponse> getAll();

    Page<UserResponse> getAll(Specification<User> specification, Pageable pageable);

    User save(UserRequest userRequest);

    void updateUser(UpdateAccountRequest userRequest);

    UserResponse findByEmail(String email);

    UserResponse findById(Long id);

    void addAddress(AddressRequest addressRequest, Long id);

    void deleteById(Long id);

    void deleteAddress(Long id);

    int countCustomer();
}
