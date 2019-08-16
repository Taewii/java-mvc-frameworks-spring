package productshop.web.interceptors;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import productshop.domain.annotations.PageTitle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class TitleInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        if (modelAndView != null && handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();

            String title = "Product Shop";
            if (method.isAnnotationPresent(PageTitle.class)) {
                PageTitle annotation = method.getAnnotation(PageTitle.class);
                title = annotation.text();
            }
            modelAndView.addObject("title", title);
        }
    }
}
