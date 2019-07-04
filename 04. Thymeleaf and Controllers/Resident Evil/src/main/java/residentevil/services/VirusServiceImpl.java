package residentevil.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import residentevil.domain.entities.Virus;
import residentevil.domain.models.binding.VirusBindingModel;
import residentevil.domain.models.view.VirusListViewModel;
import residentevil.domain.models.view.VirusViewModel;
import residentevil.repositories.VirusRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class VirusServiceImpl implements VirusService {

    private final VirusRepository virusRepository;
    private final CapitalService capitalService;
    private final ModelMapper mapper;

    @Autowired
    public VirusServiceImpl(VirusRepository virusRepository,
                            CapitalService capitalService,
                            ModelMapper mapper) {
        this.virusRepository = virusRepository;
        this.capitalService = capitalService;
        this.mapper = mapper;
    }

    @Override
    public void save(VirusBindingModel virusModel) {
        Virus virus = this.mapper.map(virusModel, Virus.class);
        virus.getCapitals().clear(); // it has 1 null value for some reason
        virusModel.getAffectedCapitals()
                .forEach(cId -> virus.addCapital(capitalService.getById(cId)));

        virusRepository.save(virus);
    }

    @Override
    public void delete(UUID id) {
        virusRepository.deleteById(id);
    }

    @Override
    public List<VirusListViewModel> findAll() {
        return virusRepository
                .findAll()
                .stream()
                .map(v -> mapper.map(v, VirusListViewModel.class)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public VirusViewModel findById(String id) {
        Virus virus = virusRepository.findById(UUID.fromString(id)).orElseThrow();
        VirusViewModel model = mapper.map(virus, VirusViewModel.class);
        virus.getCapitals().forEach(c -> model.getAffectedCapitals().add(c.getId()));
        return model;
    }
}
