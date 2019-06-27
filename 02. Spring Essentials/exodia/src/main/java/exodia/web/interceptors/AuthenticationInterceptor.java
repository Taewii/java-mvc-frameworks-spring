package exodia.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String[] GUEST_ROUTES = {"login", "register"};
    private static final String[] USER_ROUTES = {"schedule", "print", "details"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        String path = request.getRequestURI();

        if (userId != null && Arrays.stream(GUEST_ROUTES).anyMatch(path::contains)) {
            response.sendRedirect("/");
            return false;
        }

        if (userId == null && Arrays.stream(USER_ROUTES).anyMatch(path::contains)) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
