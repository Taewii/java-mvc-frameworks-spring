package exodia.services;

import exodia.domain.models.binding.DocumentScheduleBindingModel;
import exodia.domain.models.service.DocumentServiceModel;
import exodia.domain.models.view.DocumentDetailsViewModel;
import exodia.domain.models.view.DocumentHomeViewModel;
import exodia.domain.models.view.DocumentPrintViewModel;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface DocumentService {

    DocumentServiceModel schedule(DocumentScheduleBindingModel model, BindingResult result);

    DocumentDetailsViewModel findByIdDetailsModel(String id);

    List<DocumentHomeViewModel> findAll();

    DocumentPrintViewModel findByIdPrintModel(String id);

    void print(String id);
}
