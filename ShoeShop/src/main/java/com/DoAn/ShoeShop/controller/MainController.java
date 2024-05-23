package com.DoAn.ShoeShop.controller;

import com.DoAn.ShoeShop.dto.respone.CategoryResponse;
import com.DoAn.ShoeShop.dto.respone.ProductResponse;
import com.DoAn.ShoeShop.service.ICategoryService;
import com.DoAn.ShoeShop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("")
public class MainController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("")
    public String index(ModelMap modelMap){
        List<CategoryResponse> categoryResponses= categoryService.getAll();
        List<ProductResponse> newProducts = productService.getNewProduct();
        List<ProductResponse> randomProducts = productService.getRandomProduct();
        modelMap.addAttribute("newProducts", newProducts);
        modelMap.addAttribute("randomProducts", randomProducts);
        modelMap.addAttribute("categories", categoryResponses);
        return "user/homepage";
    }
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("success", "Đăng xuất thành công!");
        return "redirect:/login";
    }
}
