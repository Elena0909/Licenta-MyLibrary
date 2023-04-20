package com.example.myLibrary.mapper;

import com.example.myLibrary.model.Login;
import com.example.myLibrary.model.dto.LoginDTO;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {
    public Login convertToLogin(LoginDTO loginDTO){
        Login login = new Login();
        login.setUsername(loginDTO.getUsername());
        login.setPassword(loginDTO.getPassword());
        return login;
    }

    public LoginDTO convertTOLoginDTO(Login login){
        return new LoginDTO(login.getId(),login.getUsername(),login.getPassword());
    }
}
