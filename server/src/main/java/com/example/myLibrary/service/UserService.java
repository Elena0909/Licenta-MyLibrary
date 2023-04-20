package com.example.myLibrary.service;

import com.example.myLibrary.mapper.UserMapper;
import com.example.myLibrary.model.User;
import com.example.myLibrary.model.dto.LoginDTO;
import com.example.myLibrary.model.dto.UserDTO;
import com.example.myLibrary.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final LoginService loginService;


    public UserService(UserRepository userRepository, UserMapper mapper, LoginService loginService) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.loginService = loginService;
    }

    public Long getUserId(String username) {
        LoginDTO loginDTO = loginService.findByUsername(username);
        UserDTO userDTO = findByLoginId(loginDTO.getId());

        return userDTO.getId();
    }

    public UserDTO findByLoginId(Long id) {
        Optional<User> user = userRepository.findByLoginId(id);
        if (user.isPresent())
            return mapper.convertToUserDTO(user.get());
        throw new RuntimeException("User don't found");
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public UserDTO update(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (userDTO.getName() != null && !Objects.equals(userDTO.getName(), user.getName()))
                user.setName(userDTO.getName());
            if (userDTO.getDescription() != null && !Objects.equals(userDTO.getDescription(), user.getDescription()))
                user.setDescription(userDTO.getDescription());
            if (userDTO.getImage() != null && !Objects.equals(userDTO.getImage(), user.getImage())) {
                byte[] bytes = Base64.getDecoder().decode(userDTO.getImage());
                user.setImage(bytes);
            }

            userRepository.save(user);
            return mapper.convertToUserDTO(user);
        }

        throw new RuntimeException("User don't found");
    }

}
