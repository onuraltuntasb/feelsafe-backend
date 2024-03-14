package com.feelsafe.feelsafe.exception;

import com.feelsafe.feelsafe.model.payload.response.ApiErrorResponse;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {

    public static ResponseEntity<Object> build(ApiErrorResponse apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
