package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import productshop.domain.models.binding.AddCategoryBindingModel;
import productshop.domain.models.binding.DeleteCategoryBindingModel;
import productshop.domain.models.binding.EditCategoryBindingModel;
import productshop.domain.models.view.DeleteCategoryViewModel;
import productshop.domain.models.view.EditCategoryViewModel;
import productshop.services.CategoryService;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/all";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("category", new AddCategoryBindingModel());
        return "category/add";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute("category") AddCategoryBindingModel category, Errors errors) {
        if (errors.hasErrors()) {
            return "category/add";
        }

        boolean isSuccessful = categoryService.add(category);
        if (!isSuccessful) {
            errors.rejectValue("name", "400", "Category already exists.");
            return "category/add";
        }

        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("category",
                categoryService.findById(id, EditCategoryViewModel.class));
        return "category/edit";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/edit")
    public String editPut(@Valid @ModelAttribute("category") EditCategoryBindingModel category, Errors errors) {
        if (errors.hasErrors()) {
            return "category/edit";
        }

        boolean isSuccessful = categoryService.edit(category);
        if (!isSuccessful) {
            errors.rejectValue("name", "400", "Category already exists.");
            return "category/edit";
        }

        return "redirect:/categories/all";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, Model model) {
        model.addAttribute("category",
                categoryService.findById(id, DeleteCategoryViewModel.class));
        return "category/delete";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/delete")
    public String deleteDelete(@Valid @ModelAttribute("category") DeleteCategoryBindingModel category, Errors errors) {
        if (errors.hasErrors()) {
            return "/category/delete";
        }

        categoryService.remove(category.getName());
        return "redirect:/categories/all";
    }
}
