package com.tinexlab.tinocrm.model.dto.request;

import com.tinexlab.tinocrm.util.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String name;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role;
}
