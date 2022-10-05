package com.practice.jwttokenauth.models.user;

import com.practice.jwttokenauth.models.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "users")
public class User extends BaseModel<Long> {

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    @ToString.Exclude
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;
}
