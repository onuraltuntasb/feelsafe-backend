package com.feelsafe.feelsafe.controller;

import com.feelsafe.feelsafe.Entity.User;
import com.feelsafe.feelsafe.exception.ResourceNotFoundException;
import com.feelsafe.feelsafe.model.payload.request.RegisterRequest;
import com.feelsafe.feelsafe.model.payload.response.UserAuthResponse;
import com.feelsafe.feelsafe.repository.JdbcUserRepository;
import com.feelsafe.feelsafe.security.JwtProperties;
import com.feelsafe.feelsafe.security.JwtUtils;
import com.feelsafe.feelsafe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    JwtProperties jwtProperties = new JwtProperties();


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserAuthResponse userAuthResponse = userService.registerUser(registerRequest);
        userAuthResponse.setIsAuth(true);
        String accessToken = userAuthResponse.getJwtToken();
        accessToken = accessToken + "email=" + userAuthResponse.getEmail();

        String refreshToken = userAuthResponse.getJwtRefreshToken();
        refreshToken = refreshToken + "email=" + userAuthResponse.getEmail();


        ResponseCookie atCookie = ResponseCookie
                .from("feelsafe-at-cookie", accessToken)
                .secure(false)
                .httpOnly(true)
                .maxAge(Long.parseLong(jwtProperties.getJwtExpirationMs()) / 1000 )
                .sameSite("Strict")
                .domain("localhost")
                .path("/")
                .build();

        ResponseCookie rtCookie = ResponseCookie
                .from("feelsafe-rt-cookie", refreshToken)
                .secure(false)
                .httpOnly(true)
                .maxAge(Long.parseLong(jwtProperties.getJwtRefreshExpirationSecond()))
                .sameSite("Strict")
                .domain("localhost")
                .path("/")
                .build();


        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,atCookie.toString())
                             .header(HttpHeaders.SET_COOKIE,rtCookie.toString()).body(userAuthResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody RegisterRequest registerRequest) {

        UserAuthResponse userAuthResponse = userService.loginUser(registerRequest);
        userAuthResponse.setIsAuth(true);
        String accessToken = userAuthResponse.getJwtToken();
        accessToken = accessToken + "email=" + userAuthResponse.getEmail();

        String refreshToken = userAuthResponse.getJwtRefreshToken();
        refreshToken = refreshToken + "email=" + userAuthResponse.getEmail();

        ResponseCookie atCookie = ResponseCookie
                .from("feelsafe-at-cookie", accessToken)
                .secure(false)
                .httpOnly(true)
                .maxAge(Long.parseLong(jwtProperties.getJwtExpirationMs()) / 1000 )
                .sameSite("Strict")
                .domain("localhost")
                .path("/")
                .build();

        ResponseCookie rtCookie = ResponseCookie
                .from("feelsafe-rt-cookie", refreshToken)
                .secure(false)
                .httpOnly(true)
                .maxAge(Long.parseLong(jwtProperties.getJwtRefreshExpirationSecond()))
                .sameSite("Strict")
                .domain("localhost")
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,atCookie.toString())
                             .header(HttpHeaders.SET_COOKIE,rtCookie.toString()).body(userAuthResponse);

    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout() {


        ResponseCookie atCookie = ResponseCookie
                .from("feelsafe-at-cookie", null)
                .secure(false)
                .httpOnly(true)
                .maxAge(0 )
                .sameSite("Strict")
                .domain("localhost")
                .path("/")
                .build();

        ResponseCookie rtCookie = ResponseCookie
                .from("feelsafe-rt-cookie", null)
                .secure(false)
                .httpOnly(true)
                .maxAge(0)
                .sameSite("Strict")
                .domain("localhost")
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,atCookie.toString())
                             .header(HttpHeaders.SET_COOKIE,rtCookie.toString()).body(true);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(
            @CookieValue(name = "feelsafe-at-cookie", defaultValue = "emptyOrNull") String feelsafeAtCookie
                                      ) {
            String email = feelsafeAtCookie.substring(feelsafeAtCookie.indexOf("email")+6);
            String token = feelsafeAtCookie.substring(0,feelsafeAtCookie.indexOf("email"));


        User user = (User) jdbcUserRepository.getUserByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("user is not found with this email : " + email));

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
