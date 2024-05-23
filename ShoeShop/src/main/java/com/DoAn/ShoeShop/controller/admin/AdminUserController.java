package com.DoAn.ShoeShop.controller.admin;

import com.DoAn.ShoeShop.dto.request.UpdateAccountRequest;
import com.DoAn.ShoeShop.dto.request.UserRequest;
import com.DoAn.ShoeShop.dto.respone.RoleResponse;
import com.DoAn.ShoeShop.dto.respone.UserResponse;
import com.DoAn.ShoeShop.entity.Product;
import com.DoAn.ShoeShop.entity.User;
import com.DoAn.ShoeShop.service.IRoleService;
import com.DoAn.ShoeShop.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;

    @GetMapping("")
    public String index(@RequestParam("q") Optional<String> q,
                        @PageableDefault(size = 6) Pageable pageable,
                        ModelMap modelMap) {
        Specification<User> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isDeleted"), false);
        if (q.isPresent()) {
            Specification<User> specSearch = (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("username"), "%" + q.get() + "%");
            specification = specification.and(specSearch);
        }
        modelMap.addAttribute("users", userService.getAll(specification, pageable));
        return "admin/admin-user";
    }

    @GetMapping("/create")
    public String showCreateForm(ModelMap modelMap) {
        UserRequest userRequest = new UserRequest();
        List<RoleResponse> roleResponses = roleService.getAll();
        modelMap.addAttribute("user", userRequest);
        modelMap.addAttribute("roles", roleResponses);
        return "admin/create-user";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") @Valid UserRequest userRequest,
                             BindingResult result, RedirectAttributes redirectAttributes,
                             ModelMap modelMap) {
        if (result.hasErrors()) {
            List<RoleResponse> roleResponses = roleService.getAll();
            modelMap.addAttribute("roles", roleResponses);
            return "admin/create-user";
        }
        if (!userRequest.getPassword().equals(userRequest.getRePassword())) {
            modelMap.addAttribute("user", userRequest);
            List<RoleResponse> roleResponses = roleService.getAll();
            modelMap.addAttribute("roles", roleResponses);
            modelMap.addAttribute("rePasswordError", "Confirm password is not equal to password!");
            return "admin/create-user";
        }
        userService.save(userRequest);
        redirectAttributes.addFlashAttribute("success", "Create user successfully!");
        return "redirect:/admin/users";
    }

    @GetMapping("/edit")
    public String showUpdateForm(@RequestParam("id") Long id, ModelMap modelMap) {
        UserResponse userResponse = userService.findById(id);
        List<RoleResponse> roleResponses = roleService.getAll();
        modelMap.addAttribute("roles", roleResponses);
        modelMap.addAttribute("user", userResponse);
        return "admin/update-user";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") @Valid UpdateAccountRequest userRequest,
                             BindingResult result, ModelMap modelMap,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            modelMap.addAttribute("user", userRequest);
            List<RoleResponse> roleResponses = roleService.getAll();
            modelMap.addAttribute("roles", roleResponses);
            return "admin/update-user";
        }
        UserResponse u = userService.findByEmail(userRequest.getEmail());
        if (u != null && u.getId() != userRequest.getId()) {
            redirectAttributes.addFlashAttribute("fail", "Email is already existed!");
            return "redirect:/admin/users/edit?id="+userRequest.getId();
        }
        userService.updateUser(userRequest);
        redirectAttributes.addFlashAttribute("success", "Update user successfully!");
        return "redirect:/admin/users";
    }
    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Delete user successfully!");
        return "redirect:/admin/users";
    }
}
