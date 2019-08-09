package productshop.web.handlers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String maxUploadSizeExceptionHandler(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return "redirect:/products/add";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e) {
        e.printStackTrace();
        return "error/no-such-element-error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleForbiddenRequestException(AccessDeniedException e) {
        e.printStackTrace();
        return "error/403";
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public String handleNotFoundRequestException(HttpClientErrorException e) {
        e.printStackTrace();
        return "error/404";
    }

    @ExceptionHandler(Throwable.class)
    public String handleEveryUnhandledException(Throwable e) {
        e.printStackTrace();
        return "error/error";
    }
}
