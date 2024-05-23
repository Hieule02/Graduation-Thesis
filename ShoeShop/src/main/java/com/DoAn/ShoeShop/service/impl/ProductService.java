package com.DoAn.ShoeShop.service.impl;

import com.DoAn.ShoeShop.dto.request.ProductRequest;
import com.DoAn.ShoeShop.dto.request.ProductReviewRequest;
import com.DoAn.ShoeShop.dto.respone.ProductResponse;
import com.DoAn.ShoeShop.entity.Category;
import com.DoAn.ShoeShop.entity.Product;
import com.DoAn.ShoeShop.entity.ProductReview;
import com.DoAn.ShoeShop.repository.ProductRepository;
import com.DoAn.ShoeShop.repository.ProductReviewRepository;
import com.DoAn.ShoeShop.repository.UserRepository;
import com.DoAn.ShoeShop.service.IProductService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductReviewRepository productReviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ProductResponse> getAll() {
        return modelMapper.map(productRepository.findAllNotDeleted(), new TypeToken<List<ProductResponse>>() {
        }.getType());
    }

    @Override
    public Page<ProductResponse> getAll(Specification<Product> specification, Pageable pageable) {
        return productRepository.findAll(specification, pageable).map(entity ->
                modelMapper.map(entity, ProductResponse.class));
    }

    @Override
    public List<ProductResponse> getNewProduct() {
        return modelMapper.map(productRepository.getNewProduct(), new TypeToken<List<ProductResponse>>() {
        }.getType());
    }

    @Override
    public List<ProductResponse> getProductsByCategory_Slug(Specification<Product> specification, Sort sort) {
        return modelMapper.map(productRepository.findAll(specification, sort), new TypeToken<List<ProductResponse>>() {
        }.getType());
    }

    @Override
    public Product createProduct(ProductRequest productRequest) {
        return productRepository.save(modelMapper.map(productRequest, Product.class));
    }

    @Override
    public ProductResponse findById(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            return null;
        }
        Product product = productOpt.get();
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        return productResponse;
    }

    @Override
    public void update(ProductRequest productRequest) {
        Product product = productRepository.findById(productRequest.getId()).get();
        if (productRequest.getImage() != null) {
            product.setImage(productRequest.getImage());
        }
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setQuantity(productRequest.getQuantity());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setColors(productRequest.getColors());
        product.setSizes(productRequest.getSizes());
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).get();
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public List<ProductResponse> getRandomProduct() {
        return modelMapper.map(productRepository.getRandomProduct(), new TypeToken<List<ProductResponse>>() {
        }.getType());
    }

    @Override
    public int countProducts() {
        return productRepository.countProducts();
    }

    @Override
    public void createReview(ProductReviewRequest productReviewRequest, Long userId) {
        Product product = productRepository.findById(productReviewRequest.getProductId()).get();
        product.setRatedTimes(product.getRatedTimes() + 1);
        product.setTotalRate(product.getTotalRate() + productReviewRequest.getRating());
        ProductReview productReview = new ProductReview();
        productReview.setRating(productReviewRequest.getRating());
        productReview.setReview(productReviewRequest.getReview());
        productReview.setProduct(product);
        productReview.setUser(userRepository.findById(userId).get());
        productReviewRepository.save(productReview);
    }

    @Override
    public List<ProductReview> findReviewsByProductId(Long productId) {
        return productReviewRepository.findProductReviewsByProductId(productId);
    }

    @Override
    public List<ProductResponse> getSuggestProducts(Long id) {
        Product product = productRepository.findById(id).get();
        Set<Long> idList = new HashSet<>();
        String[] listName = product.getName().split(" ");
        for (String name : listName){
            List<Product> products = productRepository.findSuggestProduct(name);
            for (Product item : products){
                idList.add(item.getId());
            }
        }
        idList.remove(id);
        List<Product> suggestList = new ArrayList<>();
        for (Long proId : idList){
            Product pro = productRepository.findById(proId).get();
            suggestList.add(pro);
        }
        if (suggestList.isEmpty()){
            return modelMapper.map(productRepository.getTop4HighestRating(), new TypeToken<List<ProductResponse>>() {
            }.getType());
        }
        return modelMapper.map(suggestList, new TypeToken<List<ProductResponse>>() {
        }.getType());
    }

}
