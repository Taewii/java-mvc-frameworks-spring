package exodia.services;

import exodia.domain.entities.Document;
import exodia.domain.models.binding.DocumentScheduleBindingModel;
import exodia.domain.models.service.DocumentServiceModel;
import exodia.domain.models.view.DocumentDetailsViewModel;
import exodia.domain.models.view.DocumentHomeViewModel;
import exodia.domain.models.view.DocumentPrintViewModel;
import exodia.repositories.DocumentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ModelMapper mapper;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, ModelMapper mapper) {
        this.documentRepository = documentRepository;
        this.mapper = mapper;
    }

    @Override
    public DocumentServiceModel schedule(DocumentScheduleBindingModel model, BindingResult result) {
        if (result.hasErrors()) {
            return null;
        }

        Document document = this.documentRepository.saveAndFlush(this.mapper.map(model, Document.class));
        return this.mapper.map(document, DocumentServiceModel.class);
    }

    @Override
    public DocumentDetailsViewModel findByIdDetailsModel(String id) {
        Document document = this.documentRepository.findById(UUID.fromString(id)).orElse(null);
        return document == null ? null : this.mapper.map(document, DocumentDetailsViewModel.class);
    }

    @Override
    public DocumentPrintViewModel findByIdPrintModel(String id) {
        Document document = this.documentRepository.findById(UUID.fromString(id)).orElse(null);
        return document == null ? null : this.mapper.map(document, DocumentPrintViewModel.class);
    }

    @Override
    public void print(String id) {
        this.documentRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public List<DocumentHomeViewModel> findAll() {
        return this.documentRepository
                .findAll()
                .stream()
                .map(doc -> this.mapper.map(doc, DocumentHomeViewModel.class))
                .peek(doc -> {
                    if (doc.getTitle().length() > 12) {
                        doc.setTitle(doc.getTitle().substring(0, 12));
                    }
                })
                .collect(Collectors.toList());
    }
}
