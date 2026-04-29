package com.MindMate.dto.AccountDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    Long id;
    String username;
    String role;
    String token;
}
