package com.smarthome.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseData {

    private String errorMessage;
    private int errorCode;
}
