package com.DoAn.ShoeShop.service.impl;

import com.DoAn.ShoeShop.dto.request.CategoryRequest;
import com.DoAn.ShoeShop.dto.respone.CategoryResponse;
import com.DoAn.ShoeShop.entity.Category;
import com.DoAn.ShoeShop.repository.CategoryRepository;
import com.DoAn.ShoeShop.service.ICategoryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoryResponse> getAll() {
        return modelMapper.map(categoryRepository.findAll(), new TypeToken<List<CategoryResponse>>() {}.getType());
    }

    @Override
    public Category addCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setTitle(categoryRequest.getTitle());
        category.setDescription(categoryRequest.getDescription());
        category.setSlug(categoryRequest.toSlug());
        return categoryRepository.save(category);
    }

    @Override
    public CategoryResponse findById(Long id) {
        return modelMapper.map(categoryRepository.findById(id), CategoryResponse.class);
    }

    @Override
    public void updateCategory(CategoryRequest categoryRequest) throws Exception {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryRequest.getId());
        if(categoryOpt.isPresent()){
            Category category = categoryOpt.get();
            category.setTitle(categoryRequest.getTitle());
            category.setDescription(categoryRequest.getDescription());
            categoryRepository.save(category);
        } else{
            throw new Exception("Update failed!");
        }
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}
