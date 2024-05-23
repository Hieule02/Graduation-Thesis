package com.DoAn.ShoeShop.controller.admin;

import com.DoAn.ShoeShop.dto.request.CategoryRequest;
import com.DoAn.ShoeShop.dto.respone.CategoryResponse;
import com.DoAn.ShoeShop.service.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("")
    public String index(ModelMap modelMap) {
        List<CategoryResponse> categories = categoryService.getAll();
        modelMap.addAttribute("categories", categories);
        return "admin/admin-category";
    }

    @GetMapping("/create")
    public String showCreateForm(ModelMap modelMap) {
        CategoryRequest category = new CategoryRequest();
        modelMap.addAttribute("category", category);
        return "admin/create-category";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("category") @Valid CategoryRequest categoryRequest,
                         BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/create-category";
        }
        categoryService.addCategory(categoryRequest);
        redirectAttributes.addFlashAttribute("success", "Add new category successfully!");
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, ModelMap modelMap) {
        CategoryResponse categoryResponse = categoryService.findById(id);
        modelMap.addAttribute("category", categoryResponse);
        return "admin/update-category";
    }

    @PostMapping("/edit")
    public String updateCategory(@ModelAttribute("category") @Valid CategoryRequest categoryRequest,
                                 BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/update-category";
        }
        try {
            categoryService.updateCategory(categoryRequest);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", e.getMessage());
        } finally {
            return "redirect:/admin/categories";
        }
    }

    @GetMapping("/delete")
    public String deleteCategory(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("success", "Delete successfully!");
        return "redirect:/admin/categories";
    }
}
