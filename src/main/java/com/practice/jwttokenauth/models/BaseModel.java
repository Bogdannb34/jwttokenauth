package com.practice.jwttokenauth.models;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@Data
public abstract class BaseModel<I extends Serializable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private I id;
}
