package com.DoAn.ShoeShop.controller.user;

import com.DoAn.ShoeShop.dto.request.AddressRequest;
import com.DoAn.ShoeShop.dto.request.UpdateAccountRequest;
import com.DoAn.ShoeShop.dto.request.UserRequest;
import com.DoAn.ShoeShop.dto.respone.CategoryResponse;
import com.DoAn.ShoeShop.dto.respone.UserResponse;
import com.DoAn.ShoeShop.entity.Address;
import com.DoAn.ShoeShop.entity.User;
import com.DoAn.ShoeShop.security.UserSecurity;
import com.DoAn.ShoeShop.service.ICategoryService;
import com.DoAn.ShoeShop.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/register")
    public String showRegisterForm(ModelMap modelMap) {
        modelMap.addAttribute("user", new UserRequest());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid UserRequest userRequest,
                           BindingResult result, ModelMap modelMap,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/register";
        }
        if (!userRequest.getPassword().equals(userRequest.getRePassword())) {
            modelMap.addAttribute("user", userRequest);
            modelMap.addAttribute("rePasswordError", "Confirm password is not equal to password!");
            return "admin/create-user";
        }
        userRequest.setRoles(new ArrayList<>());
        userService.save(userRequest);
        redirectAttributes.addFlashAttribute("success", "Đăng kí thành công!");
        return "redirect:/users/login";
    }

    @GetMapping("/detail")
    public String detail(ModelMap modelMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();
        modelMap.addAttribute("user", userService.findById(currentUser.getId()));
        modelMap.addAttribute("categories", categoryService.getAll());
        return "user/user-detail";
    }
    @GetMapping("/update")
    public String showUpdateForm(ModelMap modelMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();
        modelMap.addAttribute("user", userService.findById(currentUser.getId()));
        modelMap.addAttribute("categories", categoryService.getAll());
        return "user/update-account";
    }
    @PostMapping("/update")
    public String updateAccount(@ModelAttribute("user") @Valid UpdateAccountRequest userRequest,
                                BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "user/update-account";
        }
        UserResponse u = userService.findByEmail(userRequest.getEmail());
        if (u != null && u.getId() != userRequest.getId()) {
            redirectAttributes.addFlashAttribute("fail", "Email is already existed!");
            return "redirect:/users/update";
        }
        userService.updateUser(userRequest);
        redirectAttributes.addFlashAttribute("success", "Update user successfully!");
        return "redirect:/users/detail";
    }

    @GetMapping("/address")
    public String showAddress(ModelMap modelMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();
        modelMap.addAttribute("user", userService.findById(currentUser.getId()));
        modelMap.addAttribute("categories", categoryService.getAll());
        modelMap.addAttribute("address", new AddressRequest());
        return "user/address";
    }

    @PostMapping("/address/create")
    public String addAddress(@ModelAttribute("address") @Valid AddressRequest addressRequest,
                             BindingResult result, ModelMap modelMap,
                             RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();
        if(result.hasErrors()){
            modelMap.addAttribute("categories", categoryService.getAll());
            modelMap.addAttribute("user", userService.findById(currentUser.getId()));
            return "user/address";
        }
        userService.addAddress(addressRequest, currentUser.getId());
        redirectAttributes.addFlashAttribute("success", "Thêm thành công!");
        return "redirect:/users/address";
    }
    @GetMapping("/address/delete")
    public String deleteAddress(@RequestParam("id") Long id, RedirectAttributes redirectAttributes,
                                ModelMap modelMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();
        userService.deleteAddress(id);
        modelMap.addAttribute("user", userService.findById(currentUser.getId()));
        redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
        return "redirect:/users/address";
    }
}
