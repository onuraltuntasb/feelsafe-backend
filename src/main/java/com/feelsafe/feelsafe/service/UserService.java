package com.feelsafe.feelsafe.service;

import com.feelsafe.feelsafe.Entity.User;
import com.feelsafe.feelsafe.exception.ResourceNotFoundException;
import com.feelsafe.feelsafe.model.RefreshToken;
import com.feelsafe.feelsafe.model.payload.request.RegisterRequest;
import com.feelsafe.feelsafe.model.payload.response.RefreshTokenResponse;
import com.feelsafe.feelsafe.model.payload.response.UserAuthResponse;
import com.feelsafe.feelsafe.repository.JdbcRefreshTokenRepository;
import com.feelsafe.feelsafe.repository.JdbcUserRepository;
import com.feelsafe.feelsafe.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@RequiredArgsConstructor
@Service
public class UserService {

    private final JdbcUserRepository jdbcUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JdbcRefreshTokenRepository jdbcRefreshTokenRepository;

    public UserAuthResponse registerUser(RegisterRequest registerRequest){
        User user = new User();

        String plainPassword = registerRequest.getPassword();
        if(plainPassword != null){
            user.setPassword(passwordEncoder.encode(plainPassword));
        }else{
            throw new ResourceNotFoundException("User password not found!");
        }

        Timestamp  timestamp = new Timestamp(System.currentTimeMillis());

        user.setEmail(registerRequest.getEmail());
        user.setCreatedAt(timestamp);
        user.setUpdatedAt(timestamp);

        User rUser = jdbcUserRepository.save(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(rUser.getId());

        UserAuthResponse userAuthResponse = new UserAuthResponse();
        userAuthResponse = UserAuthResponse.builder()
                .email(user.getEmail())
                .jwtToken(new JwtUtils().generateToken((UserDetails) rUser))
                .jwtRefreshToken(refreshToken.getToken())
                .build();

        return userAuthResponse;

    }

    public UserAuthResponse loginUser(RegisterRequest registerRequest)  {

        User rUser = (User) jdbcUserRepository
                .getUserByEmail(registerRequest.getEmail())
                .orElseThrow(() ->
                                     new ResourceNotFoundException(
                                             "Not found email with email = " + registerRequest.getEmail()));


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(), registerRequest.getPassword()
                ));

        RefreshToken refreshToken =
                jdbcRefreshTokenRepository.getByUserId(rUser.getId()).orElseThrow(
                        () -> new ResourceNotFoundException("Tag not found with this id :" + rUser.getId()));

        UserAuthResponse userAuthResponse = new UserAuthResponse();
        userAuthResponse = UserAuthResponse.builder()
                                           .email(rUser.getEmail())
                                           .jwtToken(new JwtUtils().generateToken((UserDetails) rUser))
                                           .jwtRefreshToken(refreshToken.getToken())
                                           .build();

        return userAuthResponse;
    }

    public RefreshTokenResponse getAccessTokenByRefreshToken(String refreshToken) {

        RefreshToken rToken =
                refreshTokenService.getByToken(refreshToken).orElseThrow(()->new ResourceNotFoundException("There is " +
                                                                                                                   "no refresh token!"));
        refreshTokenService.verifyExpiration(rToken);
        User user =
                (User) jdbcUserRepository.getUserByRefreshToken(refreshToken).orElseThrow(()->new ResourceNotFoundException(
                        "There is no user with this refresh token!"));

        String newAccessToken = new JwtUtils().generateToken((UserDetails) user);
        return new RefreshTokenResponse(newAccessToken, refreshToken);

    }


}
