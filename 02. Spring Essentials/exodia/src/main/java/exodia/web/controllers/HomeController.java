package exodia.web.controllers;

import exodia.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DocumentService documentService;

    @Autowired
    public HomeController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("documents", this.documentService.findAll());
        return "index";
    }

//    @GetMapping("/home")
//    public String home(Model model) {
//        model.addAttribute("documents", this.documentService.findAll());
//        return "home";
//    }
}
