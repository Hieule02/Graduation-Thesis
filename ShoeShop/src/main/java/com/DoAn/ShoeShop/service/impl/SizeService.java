package com.DoAn.ShoeShop.service.impl;

import com.DoAn.ShoeShop.dto.request.SizeRequest;
import com.DoAn.ShoeShop.dto.respone.SizeResponse;
import com.DoAn.ShoeShop.entity.Size;
import com.DoAn.ShoeShop.repository.SizeRepository;
import com.DoAn.ShoeShop.service.ISizeService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SizeService implements ISizeService {
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<SizeResponse> findAll() {
        return modelMapper.map(sizeRepository.findAll(),  new TypeToken<List<SizeResponse>>() {}.getType());
    }

    @Override
    public Page<Size> findAll(Specification<Size> specification, Pageable pageable) {
        return sizeRepository.findAll(specification, pageable);
    }

    @Override
    public Size createOrUpdate(SizeRequest sizeRequest) {
        return sizeRepository.save(modelMapper.map(sizeRequest, Size.class));
    }

    @Override
    public SizeResponse findBySize(Integer size) {
        Size siz = sizeRepository.findBySize(size);
        if(siz == null){
            return null;
        }
        return modelMapper.map(siz, SizeResponse.class);
    }

    @Override
    public void deleteById(Long id) {
        sizeRepository.deleteById(id);
    }

    @Override
    public SizeResponse findById(Long id) {
        return modelMapper.map(sizeRepository.findById(id).get(), SizeResponse.class);
    }
}
