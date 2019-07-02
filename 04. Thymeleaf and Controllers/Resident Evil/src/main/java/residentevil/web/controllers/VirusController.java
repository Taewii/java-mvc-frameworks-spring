package residentevil.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VirusController {

    @GetMapping("/add")
    public String add() {
        return "add";
    }

    @GetMapping("/show")
    public String show() {
        return "show";
    }
}
