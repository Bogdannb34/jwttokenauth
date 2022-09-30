package com.practice.jwttokenauth.models.user;

import com.practice.jwttokenauth.models.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "roles")
public class Role extends BaseModel<Long> {

    @Column(name = "role_name")
    private String roleName;
}
