package residentevil.services;

import residentevil.domain.entities.Capital;
import residentevil.domain.models.view.CapitalOptionViewModel;
import residentevil.domain.models.view.CapitalViewModel;

import java.util.List;

public interface CapitalService {

    List<CapitalOptionViewModel> getAllForOptionsMenu();

    List<CapitalViewModel> getAll();

    Capital getById(Long id);
}
