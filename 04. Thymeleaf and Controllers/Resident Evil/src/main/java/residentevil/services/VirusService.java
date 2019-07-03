package residentevil.services;

import residentevil.domain.models.binding.VirusBindingModel;
import residentevil.domain.models.view.VirusListViewModel;
import residentevil.domain.models.view.VirusViewModel;

import java.util.List;

public interface VirusService {

    void save(VirusBindingModel virus);

    List<VirusListViewModel> findAll();

    VirusViewModel findById(String id);
}
