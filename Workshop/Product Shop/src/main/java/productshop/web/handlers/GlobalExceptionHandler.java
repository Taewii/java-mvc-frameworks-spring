package productshop.web.handlers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String maxUploadSizeExceptionHandler(MaxUploadSizeExceededException e) {
        // TODO: 19.7.2019 г. needs to be handled better.
        return "redirect:/products/add";
    }
}
