package residentevil.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import residentevil.domain.entities.Virus;
import residentevil.domain.models.binding.VirusBindingModel;
import residentevil.domain.models.view.VirusViewModel;
import residentevil.repositories.VirusRepository;

import javax.transaction.Transactional;
import java.util.List;
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
        virusModel.getAffectedCapitals()
                .forEach(cId -> virus.addCapital(capitalService.getById(cId)));

        virusRepository.save(virus);
    }

    @Override
    public List<VirusViewModel> findAll() {
        return virusRepository
                .findAll()
                .stream()
                .map(v -> mapper.map(v, VirusViewModel.class)).collect(Collectors.toUnmodifiableList());
    }
}
