package com.practice.jwttokenauth.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDTO< I extends Serializable> {

    private I id;
}
