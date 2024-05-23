package com.DoAn.ShoeShop.service;

import com.DoAn.ShoeShop.dto.request.CategoryRequest;
import com.DoAn.ShoeShop.dto.respone.CategoryResponse;
import com.DoAn.ShoeShop.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICategoryService {
    List<CategoryResponse> getAll();

    Category addCategory(CategoryRequest categoryRequest);

    CategoryResponse findById(Long id);

    void updateCategory(CategoryRequest categoryRequest) throws Exception;

    void deleteCategory(Long id);
}
