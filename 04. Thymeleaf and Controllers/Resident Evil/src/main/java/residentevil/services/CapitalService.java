package residentevil.services;

import residentevil.domain.entities.Capital;
import residentevil.domain.models.view.CapitalListAllViewModel;

import java.util.List;

public interface CapitalService {

    List<CapitalListAllViewModel> findAll();

    Capital getById(Long id);
}
