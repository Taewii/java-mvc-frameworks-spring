package exodia.config;

import com.qkyrie.markdown2pdf.internal.converting.Html2PdfConverter;
import com.qkyrie.markdown2pdf.internal.converting.Markdown2HtmlConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Markdown2HtmlConverter markdown2HtmlConverter() {
        return new Markdown2HtmlConverter();
    }

    @Bean
    public Html2PdfConverter html2PdfConverter() {
        return new Html2PdfConverter();
    }
}
