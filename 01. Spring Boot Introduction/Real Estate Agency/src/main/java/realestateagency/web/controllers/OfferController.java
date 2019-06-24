package realestateagency.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import realestateagency.domain.models.binding.OfferFindBindingModel;
import realestateagency.domain.models.binding.OfferRegisterBindingModel;
import realestateagency.services.OfferService;

@Controller
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/find")
    public String find() {
        return "find.html";
    }

    @GetMapping("/reg")
    public String register() {
        return "register.html";
    }

    @PostMapping("/reg")
    public String registerPost(@ModelAttribute OfferRegisterBindingModel model) {
        return this.offerService.register(model) ? "redirect:/" : "redirect:/reg";
    }

    @PostMapping("/find")
    public String findPost(@ModelAttribute OfferFindBindingModel model) {
        return this.offerService.find(model) ? "redirect:/" : "redirect:/find";
    }
}
