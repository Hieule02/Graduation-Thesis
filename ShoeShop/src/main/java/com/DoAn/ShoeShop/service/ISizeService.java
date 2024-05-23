package com.DoAn.ShoeShop.service;

import com.DoAn.ShoeShop.dto.request.SizeRequest;
import com.DoAn.ShoeShop.dto.respone.SizeResponse;
import com.DoAn.ShoeShop.entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISizeService {

    List<SizeResponse> findAll();

    Page<Size> findAll(Specification<Size> specification, Pageable pageable);

    Size createOrUpdate(SizeRequest sizeRequest);

    SizeResponse findBySize(Integer size);

    void deleteById(Long id);

    SizeResponse findById(Long id);
}
