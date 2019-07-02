package residentevil.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import residentevil.domain.models.binding.CreateVirusBindingModel;

import javax.validation.Valid;

@Controller
public class VirusController {

    @ModelAttribute("model")
    private CreateVirusBindingModel bindingModel() {
        return new CreateVirusBindingModel();
    }

    @GetMapping("/add")
    public String add(CreateVirusBindingModel bindingModel, Model model) {
//        if (bindingModel.getName() == null) {
//            model.addAttribute()
//        }
        model.addAttribute("model", bindingModel);
        return "add";
    }

    @PostMapping("/add")
    public String addPost(@Valid CreateVirusBindingModel bindingModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("model", bindingModel);
            return "redirect:/add";
        }

        return "redirect:/";
    }

    @GetMapping("/show")
    public String show() {
        return "show";
    }
}
