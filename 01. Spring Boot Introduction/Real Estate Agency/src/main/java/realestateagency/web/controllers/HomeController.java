package realestateagency.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import realestateagency.services.OfferService;

@Controller
public class HomeController {

    private final OfferService offerService;

    @Autowired
    public HomeController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/")
    public ModelAndView index(ModelAndView model) {
        model.addObject("offers", this.offerService.findAll());
        model.setViewName("index");
        return model;
    }

//    @GetMapping("/")
//    public String index(ModelAndView model) {
//        OfferContainer container = new OfferContainer(this.offerService.findAll());
//        model.addObject("offers", container);
//        return "index.html";
//    }
}
