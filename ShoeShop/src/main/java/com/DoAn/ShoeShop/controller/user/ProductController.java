package com.DoAn.ShoeShop.controller.user;

import com.DoAn.ShoeShop.dto.request.ProductReviewRequest;
import com.DoAn.ShoeShop.dto.respone.CategoryResponse;
import com.DoAn.ShoeShop.dto.respone.ProductResponse;
import com.DoAn.ShoeShop.entity.Product;
import com.DoAn.ShoeShop.entity.ProductReview;
import com.DoAn.ShoeShop.security.UserSecurity;
import com.DoAn.ShoeShop.service.ICategoryService;
import com.DoAn.ShoeShop.service.IColorService;
import com.DoAn.ShoeShop.service.IProductService;
import com.DoAn.ShoeShop.service.ISizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ISizeService sizeService;
    @Autowired
    private IColorService colorService;
    @GetMapping("/{slug}")
    public String productByCategory(@PathVariable("slug") String slug,
                                    @RequestParam("color") Optional<Long> color,
                                    @RequestParam("siz") Optional<Long> size,
                                    @RequestParam("q") Optional<String> q,
                                    @SortDefault(sort = "name") Sort sort,
                                    ModelMap modelMap){
        List<CategoryResponse> categoryResponses = categoryService.getAll();
        Specification<Product> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("category").get("slug"), slug);
        Specification<Product> specDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isDeleted"), false);
        specification = specification.and(specDelete);
        if(q.isPresent()){
            Specification<Product> specKeyword = (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%"+q.get()+"%");
            specification = specification.and(specKeyword);
        }
        if(color.isPresent()){
            Specification<Product> specColor = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("colors").get("id"), color.get());
            specification = specification.and(specColor);
        }
        if(size.isPresent()){
            Specification<Product> specColor = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join("sizes").get("id"), size.get());
            specification = specification.and(specColor);
        }
        List<ProductResponse> productResponses = productService.getProductsByCategory_Slug(specification, sort);
        modelMap.addAttribute("products", productResponses);
        modelMap.addAttribute("colors", colorService.findAll());
        modelMap.addAttribute("sizes", sizeService.findAll());
        modelMap.addAttribute("categories", categoryResponses);
        return "user/products";
    }
    @GetMapping("/detail")
    public String productDetail(@RequestParam("id") Long id, ModelMap modelMap){
        ProductResponse productResponse = productService.findById(id);
        List<CategoryResponse> categoryResponses = categoryService.getAll();
        List<ProductReview> productReviews = productService.findReviewsByProductId(id);
        List<ProductResponse> relatedProducts = productService.getSuggestProducts(id);

        modelMap.addAttribute("product", productResponse);
        modelMap.addAttribute("reviews", productReviews);
        modelMap.addAttribute("relatedProducts", relatedProducts);
        modelMap.addAttribute("categories", categoryResponses);
        return "user/product-detail";
    }

    @PostMapping("/review")
    public String createReview(@ModelAttribute ProductReviewRequest productReviewRequest,
                               RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();

        productService.createReview(productReviewRequest, currentUser.getId());
        redirectAttributes.addFlashAttribute("success", "Gửi đánh giá thành công!");
        return "redirect:/products/detail?id=" + productReviewRequest.getProductId();
    }
}
