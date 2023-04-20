package com.example.myLibrary.service;

import com.example.myLibrary.mapper.LoginMapper;
import com.example.myLibrary.mapper.UserMapper;
import com.example.myLibrary.model.Login;
import com.example.myLibrary.model.User;
import com.example.myLibrary.model.dto.LoginDTO;
import com.example.myLibrary.model.dto.UserDTO;
import com.example.myLibrary.repository.LoginRepository;
import com.example.myLibrary.repository.UserRepository;
import com.example.myLibrary.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final LoginRepository loginRepository;
    private final UserRepository userRepository;
    private final LoginMapper loginMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public LoginService(LoginRepository loginRepository, UserRepository userRepository, LoginMapper loginMapper, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.loginRepository = loginRepository;
        this.userRepository = userRepository;
        this.loginMapper = loginMapper;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


//    public boolean existUsername(String username){
//        return loginRepository.findByUsername(username).isPresent();
//    }

    public LoginDTO findByUsername(String username) {
        Optional<Login> login = loginRepository.findByUsername(username);

        if (login.isPresent())
            return loginMapper.convertTOLoginDTO(login.get());
        else
            throw new RuntimeException("Username don't exists!");
    }

    public String register(LoginDTO loginDTO) {
        loginRepository.findByUsername(loginDTO.getUsername()).ifPresent(i -> {
            throw new RuntimeException("Username already exists!");
        });

        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            throw new RuntimeException("Username can not be null!");
        }

        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            throw new RuntimeException("Password can not be null!");
        }
        if (loginDTO.getPassword().length() < 6) {
            throw new RuntimeException("Choose a strong password, min 6 characters");
        }

        Login login = loginMapper.convertToLogin(loginDTO);
        login.setPassword(passwordEncoder.encode(login.getPassword()));
        loginRepository.save(login);
        User user = new User();
        user.setLogin(login);
        userRepository.save(user);

        return login(loginDTO);
    }

    public String login(LoginDTO loginDTO) {
        Optional<Login> optionalLogin = loginRepository.findByUsername(loginDTO.getUsername());
        if (optionalLogin.isEmpty())
            throw new RuntimeException("Bad credentials");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        return jwtTokenProvider.generateToken(authentication);
    }

}
