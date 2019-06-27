package exodia.web.controllers;

import exodia.domain.models.binding.DocumentScheduleBindingModel;
import exodia.domain.models.service.DocumentServiceModel;
import exodia.domain.models.view.DocumentDetailsViewModel;
import exodia.domain.models.view.DocumentPrintViewModel;
import exodia.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/schedule")
    public String schedule() {
        return "schedule";
    }

    @PostMapping("/schedule")
    public String schedulePost(@Valid @ModelAttribute DocumentScheduleBindingModel model, BindingResult result) {
        DocumentServiceModel doc = this.documentService.schedule(model, result);
        if (doc != null) {
            return "redirect:/details/" + doc.getId();
        }

        return "redirect;/schedule";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable String id, Model model) {
        DocumentDetailsViewModel document = this.documentService.findByIdDetailsModel(id);
        if (document == null) {
            return "redirect:/";
        }

        model.addAttribute("document", document);
        return "details";
    }

    @GetMapping("/print/{id}")
    public String print(@PathVariable String id, Model model) {
        DocumentPrintViewModel document = this.documentService.findByIdPrintModel(id);
        if (document == null) {
            return "redirect:/";
        }

        model.addAttribute("document", document);
        return "print";
    }

    @PostMapping("/print/{id}")
    public String printPost(@PathVariable String id) {
        this.documentService.print(id);
        return "redirect:/";
    }

    @GetMapping("/print/pdf/{id}")
    public ResponseEntity<byte[]> getAsPdf(@PathVariable String id) {
        byte[] contents = this.documentService.getPdf(id).orElse(null);

        if (contents == null) {
            throw new IllegalArgumentException("Something is wrong with the formatting of the document, extraction is not possible.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "document-" + id.substring(0, 8) + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }
}
