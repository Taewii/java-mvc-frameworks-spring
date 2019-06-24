package realestateagency.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import realestateagency.domain.entities.Offer;
import realestateagency.domain.models.binding.OfferFindBindingModel;
import realestateagency.domain.models.binding.OfferRegisterBindingModel;
import realestateagency.domain.models.view.OfferHomeViewModel;
import realestateagency.repositories.OfferRepository;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository,
                            Validator validator,
                            ModelMapper mapper) {
        this.offerRepository = offerRepository;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public boolean register(OfferRegisterBindingModel model) {
        if (!this.validator.validate(model).isEmpty()) return false;
        Offer entity = this.mapper.map(model, Offer.class);
        this.offerRepository.saveAndFlush(entity);
        return true;
    }

    @Override
    public boolean find(OfferFindBindingModel model) {
        if (!this.validator.validate(model).isEmpty()) return false;

        Offer foundOffer = this.offerRepository
                .findAllByApartmentType(model.getFamilyApartmentType())
                .stream()
                .filter(offer -> model.getFamilyBudget().compareTo(calculateOfferPrice(offer)) >= 0 &&
                        model.getFamilyApartmentType().equals(offer.getApartmentType()))
                .findFirst()
                .orElse(null);

        if (foundOffer != null) {
            this.offerRepository.delete(foundOffer);
            return true;
        }

        return false;
    }

    @Override
    public List<OfferHomeViewModel> findAll() {
        return this.offerRepository
                .findAll()
                .parallelStream()
                .map(offer -> this.mapper.map(offer, OfferHomeViewModel.class))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateOfferPrice(Offer offer) {
        BigDecimal commissionPercentage = offer.getApartmentCommission().multiply(BigDecimal.valueOf(0.01));
        return offer.getApartmentRent().multiply(BigDecimal.ONE.add(commissionPercentage));
    }
}
