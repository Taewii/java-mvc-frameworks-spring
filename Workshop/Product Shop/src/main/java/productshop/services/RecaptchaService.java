package productshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecaptchaService {

    private static final Map<String, String> RECAPTCHA_ERROR_CODE = new HashMap<>() {{
        put("missing-input-secret", "The secret parameter is missing");
        put("invalid-input-secret", "The secret parameter is invalid or malformed");
        put("missing-input-response", "The response parameter is missing");
        put("invalid-input-response", "The response parameter is invalid or malformed");
        put("bad-request", "The request is invalid or malformed");
    }};

    private static final String GOOGLE_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    public RecaptchaService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    public String verifyRecaptcha(String ip, String recaptchaResponse) {
        Map<String, String> body = new HashMap<>();
        body.put("secret", recaptchaSecret);
        body.put("response", recaptchaResponse);
        body.put("remoteip", ip);

        ResponseEntity<Map> recaptchaResponseEntity =
                restTemplateBuilder.build()
                        .postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL +
                                        "?secret={secret}&response={response}&remoteip={remoteip}",
                                body, Map.class, body);

        Map<String, Object> responseBody = recaptchaResponseEntity.getBody();

        boolean recaptchaSuccess = (Boolean) responseBody.get("success");
        if (!recaptchaSuccess) {
            List<String> errorCodes = (List) responseBody.get("error-codes");

            return errorCodes
                    .stream()
                    .map(RECAPTCHA_ERROR_CODE::get)
                    .collect(Collectors.joining(", "));
        } else {
            return "";
        }
    }
}