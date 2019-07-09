package residentevil.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import residentevil.domain.models.view.CapitalViewModel;
import residentevil.domain.models.view.UserViewModel;
import residentevil.domain.models.view.VirusListViewModel;
import residentevil.services.CapitalService;
import residentevil.services.UserService;
import residentevil.services.VirusService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ListingController {

    private final VirusService virusService;
    private final CapitalService capitalService;

    public ListingController(VirusService virusService,
                             CapitalService capitalService) {
        this.virusService = virusService;
        this.capitalService = capitalService;
    }

    @GetMapping("/viruses")
    public List<VirusListViewModel> showViruses() {
        return virusService.findAll();
    }

    @GetMapping("/capitals")
    public List<CapitalViewModel> showCapitals() {
        return capitalService.findAll();
    }
}
