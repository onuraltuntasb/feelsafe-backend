package com.feelsafe.feelsafe.controller;

import com.feelsafe.feelsafe.exception.ResourceNotFoundException;
import com.feelsafe.feelsafe.model.User;
import com.feelsafe.feelsafe.model.payload.request.RegisterRequest;
import com.feelsafe.feelsafe.model.payload.response.UserAuthResponse;
import com.feelsafe.feelsafe.repository.JdbcUserRepository;
import com.feelsafe.feelsafe.security.JwtUtils;
import com.feelsafe.feelsafe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JdbcUserRepository jdbcUserRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserAuthResponse userAuthResponse = userService.registerUser(registerRequest);
        userAuthResponse.setIsAuth(true);
        String auth = userAuthResponse.getJwtToken();
        auth = auth + "email=" + userAuthResponse.getEmail();

        ResponseCookie cookie = ResponseCookie
                .from("feelsafe-auth-cookie", auth)
                .secure(false)
                .httpOnly(true)
                .maxAge(86400)
                .sameSite("Strict")
                .domain("localhost")
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(userAuthResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserAuthResponse userAuthResponse = userService.loginUser(registerRequest);
        userAuthResponse.setIsAuth(true);
        String auth = userAuthResponse.getJwtToken();
        auth = auth + "email=" + userAuthResponse.getEmail();

        ResponseCookie cookie = ResponseCookie
                .from("feelsafe-auth-cookie", auth)
                .secure(false)
                .httpOnly(true)
                .maxAge(86400)
                .sameSite("Strict")
                .domain("localhost")
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(userAuthResponse);

    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout() {

        ResponseCookie cookie = ResponseCookie
                .from("feelsafe-auth-cookie", null)
                .secure(false)
                .httpOnly(true)
                .maxAge(86400)
                .sameSite("Strict")
                .domain("localhost")
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(true);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(
            @CookieValue(name = "feelsafe-auth-cookie", defaultValue = "emptyOrNull") String feelsafeAuthCookie
                                      ) {
            String email = feelsafeAuthCookie.substring(feelsafeAuthCookie.indexOf("email")+6);
            String token = feelsafeAuthCookie.substring(0,feelsafeAuthCookie.indexOf("email"));


        User user = (User) jdbcUserRepository.getUserByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("user is not found with this email : " + email));

        UserAuthResponse userAuthResponse = UserAuthResponse
                .builder()
                .email(user.getEmail())
                .isAuth(new JwtUtils().isTokenValid(token, email))
                .build();

        return ResponseEntity.ok(userAuthResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody String refreshToken) {
        return ResponseEntity.ok(userService.getAccessTokenByRefreshToken(refreshToken));
    }
}
