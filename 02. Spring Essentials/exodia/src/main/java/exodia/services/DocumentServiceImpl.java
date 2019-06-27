package exodia.services;

import com.qkyrie.markdown2pdf.internal.converting.Html2PdfConverter;
import com.qkyrie.markdown2pdf.internal.converting.Markdown2HtmlConverter;
import com.qkyrie.markdown2pdf.internal.exceptions.ConversionException;
import exodia.domain.entities.Document;
import exodia.domain.models.binding.DocumentScheduleBindingModel;
import exodia.domain.models.service.DocumentPrintServiceModel;
import exodia.domain.models.service.DocumentServiceModel;
import exodia.domain.models.view.DocumentDetailsViewModel;
import exodia.domain.models.view.DocumentHomeViewModel;
import exodia.domain.models.view.DocumentPrintViewModel;
import exodia.repositories.DocumentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.xhtmlrenderer.util.XRRuntimeException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ModelMapper mapper;
    private final Html2PdfConverter html2PdfConverter;
    private final Markdown2HtmlConverter markdown2HtmlConverter;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, ModelMapper mapper,
                               Html2PdfConverter html2PdfConverter,
                               Markdown2HtmlConverter markdown2HtmlConverter) {
        this.documentRepository = documentRepository;
        this.mapper = mapper;
        this.html2PdfConverter = html2PdfConverter;
        this.markdown2HtmlConverter = markdown2HtmlConverter;
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
    public Optional<byte[]> getPdf(String id) {
        Document document = this.documentRepository.findById(UUID.fromString(id)).orElse(null);

        if (document == null) {
            return Optional.empty();
        }

        DocumentPrintServiceModel doc = this.mapper.map(document, DocumentPrintServiceModel.class);
        String content = String.format("<h1 style=\"text-align: center;\">%s</h1>\n", doc.getTitle()) + doc.getContent();

        try {
            String html = this.markdown2HtmlConverter.convert(content);
            html = "<div>" + html.replaceAll("(?<=</h[0-6])>", ">\n<hr />\n") + "</div>";
            return Optional.of(this.html2PdfConverter.convert(html));
        } catch (ConversionException | XRRuntimeException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<DocumentHomeViewModel> findAll() {
        return this.documentRepository
                .findAll()
                .stream()
                .map(doc -> this.mapper.map(doc, DocumentHomeViewModel.class))
                .peek(doc -> {
                    if (doc.getTitle().length() > 12) {
                        doc.setTitle(doc.getTitle().substring(0, 12) + "...");
                    }
                })
                .collect(Collectors.toList());
    }
}
