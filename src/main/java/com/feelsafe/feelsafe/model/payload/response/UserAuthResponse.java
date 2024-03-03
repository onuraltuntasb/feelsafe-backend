package com.feelsafe.feelsafe.model.payload.response;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserAuthResponse {

    private String email;
    private String name;
    private String jwtToken;
    private String jwtRefreshToken;
    private Boolean isAuth;

}

