package com.schoolmoney.pl.core.csrf;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class CsrfController {
    @GetMapping("/csrf")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Object> csrf(CsrfToken csrfToken, HttpServletRequest request) {
        String url = request.getServerName();
        String regex = "(?:[^.]+\\.)?([^.]+\\.[^.]+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        String domain = null;

        if (matcher.find()) {
            domain = matcher.group(1);
        }

        return ResponseEntity.ok()
                .header("Set-Cookie",
                        "XSRF-TOKEN" + "=" + "123" + "; Path=/; SameSite=None; Secure; Domain=" + domain)
                .body(null);
    }
}

