package residentevil.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import residentevil.domain.models.binding.VirusBindingModel;
import residentevil.domain.models.view.CapitalOptionViewModel;
import residentevil.services.CapitalService;
import residentevil.services.VirusService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/virus")
public class VirusController {

    private static final String VIEW_ADD = "virus/add";
    private static final String VIEW_EDIT = "virus/edit";
    private static final String VIEW_DELETE = "virus/delete";

    private final CapitalService capitalService;
    private final VirusService virusService;

    @Autowired
    public VirusController(CapitalService capitalService, VirusService virusService) {
        this.capitalService = capitalService;
        this.virusService = virusService;
    }

    @ModelAttribute("capitals")
    public List<CapitalOptionViewModel> capitals() {
        return capitalService.getAllForOptionsMenu();
    }

    @GetMapping("/show")
    public String show() {
        return "show";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("virus", new VirusBindingModel());
        return VIEW_ADD;
    }

    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute("virus") VirusBindingModel bindingModel, Errors errors) {
        if (errors.hasErrors()) {
            return VIEW_ADD;
        }

        virusService.save(bindingModel);
        return "redirect:/virus/show";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("virus", virusService.findById(id));
        return VIEW_EDIT;
    }

    @PutMapping("/edit")
    public String editPut(@Valid @ModelAttribute("virus") VirusBindingModel bindingModel,
                          Errors errors) {
        if (errors.hasErrors()) {
            return VIEW_EDIT;
        }

        virusService.save(bindingModel);
        return "redirect:/virus/show";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, Model model) {
        model.addAttribute("virus", virusService.findById(id));
        return VIEW_DELETE;
    }

    @DeleteMapping("/delete")
    public String deleteDelete(@ModelAttribute VirusBindingModel bindingModel) {
        virusService.delete(bindingModel.getId());
        return "redirect:/virus/show";
    }
}
