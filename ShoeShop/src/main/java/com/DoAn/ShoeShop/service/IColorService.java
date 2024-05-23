package com.DoAn.ShoeShop.service;

import com.DoAn.ShoeShop.dto.request.ColorRequest;
import com.DoAn.ShoeShop.dto.respone.ColorResponse;
import com.DoAn.ShoeShop.entity.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface IColorService {
    Page<Color> findAll(Specification<Color> specification, Pageable pageable);

    List<Color> findAll();

    Color createOrUpdate(ColorRequest colorRequest);

    ColorResponse findById(Long id);

    void deleteById(Long id);

    ColorResponse findByColor(String color);
}
