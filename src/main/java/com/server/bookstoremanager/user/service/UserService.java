package com.server.bookstoremanager.user.service;

import com.server.bookstoremanager.user.dto.MessageDTO;
import com.server.bookstoremanager.user.dto.UserDTO;
import com.server.bookstoremanager.user.entity.User;
import com.server.bookstoremanager.user.exception.UserAlreadyExistsException;
import com.server.bookstoremanager.user.exception.UserNotFoundException;
import com.server.bookstoremanager.user.mapper.UserMapper;
import com.server.bookstoremanager.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    public static final UserMapper userMapper = UserMapper.INSTANCE;

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MessageDTO create(UserDTO userDTO) {
        verifyIfExists(userDTO.getEmail(), userDTO.getUsername());
        User userToCreate = userMapper.toModel(userDTO);
        User createdUser = userRepository.save(userToCreate);
        return creationMessage(createdUser);
    }

    public void delete(Long id) {
        verifyIfExists(id);
        userRepository.deleteById(id);
    }

    private void verifyIfExists(String email, String username) {
        Optional<User> foundUser = userRepository.findByEmailOrUsername(email, username);
        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException(email, username);
        }
    }

    private void verifyIfExists(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private MessageDTO creationMessage(User createdUser) {
        String username = createdUser.getUsername();
        Long id = createdUser.getId();
        String createdUserMessage = String.format("User %s with id %s successfully created", username, id);
        return MessageDTO.builder()
                .message(createdUserMessage)
                .build();
    }
}
