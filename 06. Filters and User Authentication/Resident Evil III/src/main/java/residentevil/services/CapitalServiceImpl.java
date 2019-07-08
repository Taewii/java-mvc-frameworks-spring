package residentevil.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import residentevil.domain.entities.Capital;
import residentevil.domain.models.view.CapitalOptionViewModel;
import residentevil.domain.models.view.CapitalViewModel;
import residentevil.repositories.CapitalRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CapitalServiceImpl implements CapitalService {

    private final CapitalRepository capitalRepository;
    private final ModelMapper mapper;

    @Autowired
    public CapitalServiceImpl(CapitalRepository capitalRepository, ModelMapper mapper) {
        this.capitalRepository = capitalRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CapitalOptionViewModel> getAllForOptionsMenu() {
        return capitalRepository.findAllByOrderByName()
                .stream()
                .map(c -> mapper.map(c, CapitalOptionViewModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<CapitalViewModel> findAll() {
        return capitalRepository.findAllByOrderByName()
                .stream()
                .map(c -> mapper.map(c, CapitalViewModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Capital getById(Long id) {
        return capitalRepository.findById(id).orElseThrow();
    }
}
