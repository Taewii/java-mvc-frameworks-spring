package residentevil.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import residentevil.domain.models.binding.VirusBindingModel;
import residentevil.domain.models.view.CapitalListAllViewModel;
import residentevil.services.CapitalService;
import residentevil.services.VirusService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/virus")
public class VirusController {

    private final CapitalService capitalService;
    private final VirusService virusService;

    @Autowired
    public VirusController(CapitalService capitalService, VirusService virusService) {
        this.capitalService = capitalService;
        this.virusService = virusService;
    }

    @ModelAttribute("capitals")
    public List<CapitalListAllViewModel> capitals() {
        return capitalService.findAll();
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("virus", new VirusBindingModel());
        return "add";
    }

    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute("virus") VirusBindingModel bindingModel, Errors errors) {
        if (errors.hasErrors()) {
            return "add";
        }

        virusService.save(bindingModel);
        return "redirect:/";
    }

    @GetMapping("/show")
    public String show(Model model) {
        model.addAttribute("viruses", virusService.findAll());
        return "show";
    }
}
