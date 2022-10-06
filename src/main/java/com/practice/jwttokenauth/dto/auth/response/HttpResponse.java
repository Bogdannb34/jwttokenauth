package com.practice.jwttokenauth.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class HttpResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:ss:mm", timezone = "Europe/Bucharest")
    private Date timeStamp;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String response;
    private String message;

    public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String response, String message) {
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.response = response;
        this.message = message;
    }
}
