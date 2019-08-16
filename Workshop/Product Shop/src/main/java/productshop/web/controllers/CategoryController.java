package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import productshop.domain.annotations.PageTitle;
import productshop.domain.models.binding.category.AddCategoryBindingModel;
import productshop.domain.models.binding.category.DeleteCategoryBindingModel;
import productshop.domain.models.binding.category.EditCategoryBindingModel;
import productshop.domain.models.view.category.DeleteCategoryViewModel;
import productshop.domain.models.view.category.EditCategoryViewModel;
import productshop.services.CategoryService;

import javax.validation.Valid;

import static productshop.config.Constants.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private static final String CATEGORY_VIEWS_PATH = "category";
    private static final String ALL_VIEW = CATEGORY_VIEWS_PATH + "/" + "all";
    private static final String ADD_VIEW = CATEGORY_VIEWS_PATH + "/" + "add";
    private static final String EDIT_VIEW = CATEGORY_VIEWS_PATH + "/" + "edit";
    private static final String DELETE_VIEW = CATEGORY_VIEWS_PATH + "/" + "delete";

    private static final String NAME_ATTRIBUTE = "name";
    private static final String CATEGORY_ATTRIBUTE = "category";
    private static final String CATEGORIES_ATTRIBUTE = "categories";

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PageTitle(text = "All Categories")
    @PreAuthorize(IS_MODERATOR)
    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute(CATEGORIES_ATTRIBUTE, categoryService.findAll());
        return ALL_VIEW;
    }

    @PageTitle(text = "Add Category")
    @PreAuthorize(IS_MODERATOR)
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute(CATEGORY_ATTRIBUTE, new AddCategoryBindingModel());
        return ADD_VIEW;
    }

    @PreAuthorize(IS_MODERATOR)
    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute(CATEGORY_ATTRIBUTE) AddCategoryBindingModel category, Errors errors) {
        if (errors.hasErrors()) {
            return ADD_VIEW;
        }

        boolean isSuccessful = categoryService.add(category);
        if (!isSuccessful) {
            errors.rejectValue(NAME_ATTRIBUTE, BAD_REQUEST_ERROR_CODE, CATEGORY_ALREADY_EXISTS_MESSAGE);
            return ADD_VIEW;
        }

        return "redirect:/home";
    }

    @PageTitle(text = "Edit Category")
    @PreAuthorize(IS_MODERATOR)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute(CATEGORY_ATTRIBUTE, categoryService.findById(id, EditCategoryViewModel.class));
        return EDIT_VIEW;
    }

    @PreAuthorize(IS_MODERATOR)
    @PutMapping("/edit")
    public String editPut(@Valid @ModelAttribute(CATEGORY_ATTRIBUTE) EditCategoryBindingModel category, Errors errors) {
        if (errors.hasErrors()) {
            return EDIT_VIEW;
        }

        boolean isSuccessful = categoryService.edit(category);
        if (!isSuccessful) {
            errors.rejectValue(NAME_ATTRIBUTE, BAD_REQUEST_ERROR_CODE, CATEGORY_ALREADY_EXISTS_MESSAGE);
            return EDIT_VIEW;
        }

        return "redirect:/categories/all";
    }

    @PageTitle(text = "Delete Category")
    @PreAuthorize(IS_MODERATOR)
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, Model model) {
        model.addAttribute(CATEGORY_ATTRIBUTE, categoryService.findById(id, DeleteCategoryViewModel.class));
        return DELETE_VIEW;
    }

    @PreAuthorize(IS_MODERATOR)
    @DeleteMapping("/delete")
    public String deleteDelete(@Valid @ModelAttribute(CATEGORY_ATTRIBUTE)
                                       DeleteCategoryBindingModel category,
                               Errors errors) {
        if (errors.hasErrors()) {
            return DELETE_VIEW;
        }

        categoryService.remove(category.getName());
        return "redirect:/categories/all";
    }
}
