package com.example.myLibrary.mapper;

import com.example.myLibrary.model.User;
import com.example.myLibrary.model.dto.UserDTO;
import com.example.myLibrary.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Optional;

@Component
public class UserMapper {
    private final UserRepository userRepository;

    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User concerToUser(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            User user = new User();
            user.setName(userDTO.getName());
            user.setDescription(userDTO.getDescription());
            byte[] bytes = Base64.getDecoder().decode(userDTO.getImage());
            user.setImage(bytes);
            return user;
        } else {
            Optional<User> optional = userRepository.findById(userDTO.getId());
            if(optional.isPresent()) {
                return optional.get();
            }
            throw new RuntimeException("Error server");
        }
    }

    public UserDTO convertToUserDTO(User user){
        String image;
        if(user.getImage()==null)
            image = "";
        else image = Base64.getEncoder().encodeToString(user.getImage());
        return new UserDTO(user.getId(),user.getName(),user.getDescription(),image);
    }
}
