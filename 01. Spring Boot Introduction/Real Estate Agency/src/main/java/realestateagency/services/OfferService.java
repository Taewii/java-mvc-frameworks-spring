package realestateagency.services;

import realestateagency.domain.models.binding.OfferFindBindingModel;
import realestateagency.domain.models.binding.OfferRegisterBindingModel;
import realestateagency.domain.models.view.OfferHomeViewModel;

import java.util.List;

public interface OfferService {

    boolean register(OfferRegisterBindingModel model);

    boolean find(OfferFindBindingModel model);

    List<OfferHomeViewModel> findAll();
}
