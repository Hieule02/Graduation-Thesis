package com.DoAn.ShoeShop.service.impl;

import com.DoAn.ShoeShop.dto.request.ColorRequest;
import com.DoAn.ShoeShop.dto.respone.ColorResponse;
import com.DoAn.ShoeShop.entity.Color;
import com.DoAn.ShoeShop.repository.ColorRepository;
import com.DoAn.ShoeShop.service.IColorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ColorService implements IColorService {
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Page<Color> findAll(Specification<Color> specification, Pageable pageable) {
        return colorRepository.findAll(specification, pageable);
    }

    public List<Color> findAll(){
        return colorRepository.findAll();
    }

    @Override
    public Color createOrUpdate(ColorRequest colorRequest) {
        return colorRepository.save(modelMapper.map(colorRequest, Color.class));
    }

    @Override
    public ColorResponse findById(Long id) {
        return modelMapper.map(colorRepository.findById(id), ColorResponse.class);
    }

    @Override
    public void deleteById(Long id) {
        colorRepository.deleteById(id);
    }

    @Override
    public ColorResponse findByColor(String color) {
        Color colour = colorRepository.findByColor(color);
        if(colour == null){
            return null;
        }
        return modelMapper.map(colour, ColorResponse.class);
    }

}
