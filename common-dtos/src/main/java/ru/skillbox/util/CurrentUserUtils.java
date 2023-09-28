package ru.skillbox.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

@UtilityClass
public class CurrentUserUtils {

    private final String SECRET = "B9vPHGM8C0ia4uOuxxqPD5DTbWC9F9TWvPStp3pb7ARo0oK2mJ3pd3YG4lxA9i8bj6OTbadwezxgeEByY";

    public String getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String user = "";
        if (token != null) {
            user = JWT.require(algorithm())
                    .build()
                    .verify(token.replace("Bearer ", ""))
                    .getSubject();
        }
        return user;

    }

    private Algorithm algorithm(){
        return Algorithm.HMAC512(SECRET.getBytes());
    }
}
