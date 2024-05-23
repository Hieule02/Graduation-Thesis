package com.DoAn.ShoeShop.controller.admin;

import com.DoAn.ShoeShop.dto.request.ProductRequest;
import com.DoAn.ShoeShop.dto.respone.CategoryResponse;
import com.DoAn.ShoeShop.dto.respone.ColorResponse;
import com.DoAn.ShoeShop.dto.respone.ProductResponse;
import com.DoAn.ShoeShop.dto.respone.SizeResponse;
import com.DoAn.ShoeShop.entity.Color;
import com.DoAn.ShoeShop.entity.Product;
import com.DoAn.ShoeShop.service.ICategoryService;
import com.DoAn.ShoeShop.service.IColorService;
import com.DoAn.ShoeShop.service.IProductService;
import com.DoAn.ShoeShop.service.ISizeService;
import jakarta.validation.Valid;
import org.hibernate.sql.exec.spi.StandardEntityInstanceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ISizeService sizeService;
    @Autowired
    private IColorService colorService;
    private final String uploadDir = "src/main/resources/images";

    @GetMapping("")
    public String index(@RequestParam("q") Optional<String> q,
                        @PageableDefault(size = 5) Pageable pageable,
                        ModelMap modelMap) {
        Specification<Product> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isDeleted"), false);
        if (q.isPresent()) {
            Specification<Product> specSearch = (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + q.get() + "%");
            specification = specification.and(specSearch);
        }
        Page<ProductResponse> productResponses = productService.getAll(specification, pageable);
        modelMap.addAttribute("products", productResponses);
        return "admin/admin-product";
    }

    @GetMapping("/create")
    public String showCreateForm(ModelMap modelMap) {
        ProductRequest productRequest = new ProductRequest();
        List<CategoryResponse> categoryResponses = categoryService.getAll();
        List<Color> colorResponses = colorService.findAll();
        List<SizeResponse> sizeResponses = sizeService.findAll();
        modelMap.addAttribute("sizes", sizeResponses);
        modelMap.addAttribute("colors", colorResponses);
        modelMap.addAttribute("product", productRequest);
        modelMap.addAttribute("categories", categoryResponses);
        return "admin/create-product";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute("product") @Valid ProductRequest productRequest,
                                BindingResult result, @RequestParam("imageFile") MultipartFile file,
                                RedirectAttributes redirectAttributes, ModelMap modelMap) throws IOException {
        if (result.hasErrors()) {
            List<CategoryResponse> categoryResponses = categoryService.getAll();
            List<Color> colorResponses = colorService.findAll();
            List<SizeResponse> sizeResponses = sizeService.findAll();
            modelMap.addAttribute("sizes", sizeResponses);
            modelMap.addAttribute("colors", colorResponses);
            modelMap.addAttribute("categories", categoryResponses);
            return "admin/create-product";
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String storageFileName = fileName;
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + "/" + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IOException("Could not upload file:" + fileName);
        }

        productRequest.setImage(storageFileName);
        productService.createProduct(productRequest);
        redirectAttributes.addFlashAttribute("success", "Create new product successfully!");
        return "redirect:/admin/products";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, ModelMap modelMap) {
        ProductResponse productResponse = productService.findById(id);
        List<CategoryResponse> categoryResponses = categoryService.getAll();
        List<Color> colorResponses = colorService.findAll();
        List<SizeResponse> sizeResponses = sizeService.findAll();
        modelMap.addAttribute("sizes", sizeResponses);
        modelMap.addAttribute("colors", colorResponses);
        modelMap.addAttribute("product", productResponse);
        modelMap.addAttribute("categories", categoryResponses);
        return "admin/update-product";
    }

    @PostMapping("/edit")
    public String updateProduct(@ModelAttribute("product") @Valid ProductRequest productRequest,
                                BindingResult result, ModelMap modelMap,
                                RedirectAttributes redirectAttributes,
                                @RequestParam("imageFile") MultipartFile file) throws IOException {
        if (result.hasErrors()) {
            List<CategoryResponse> categoryResponses = categoryService.getAll();
            List<Color> colorResponses = colorService.findAll();
            List<SizeResponse> sizeResponses = sizeService.findAll();
            modelMap.addAttribute("sizes", sizeResponses);
            modelMap.addAttribute("colors", colorResponses);
            modelMap.addAttribute("categories", categoryResponses);
            return "admin/update-product";
        }
        if (StringUtils.cleanPath(file.getOriginalFilename()) != "") {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String storageFileName = fileName;
            try {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + "/" + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new IOException("Could not upload file:" + fileName);
            }
            productRequest.setImage(storageFileName);
        }
        productService.update(productRequest);
        redirectAttributes.addFlashAttribute("success", "Update successfully!");
        return "redirect:/admin/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("success", "Delete successfully!");
        return "redirect:/admin/products";
    }
}
