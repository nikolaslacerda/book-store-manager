package com.server.bookstoremanager.user.service;

import com.server.bookstoremanager.user.dto.MessageDTO;
import com.server.bookstoremanager.user.dto.UserDTO;
import com.server.bookstoremanager.user.entity.User;
import com.server.bookstoremanager.user.exception.UserAlreadyExistsException;
import com.server.bookstoremanager.user.exception.UserNotFoundException;
import com.server.bookstoremanager.user.mapper.UserMapper;
import com.server.bookstoremanager.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.server.bookstoremanager.user.util.MessageUtils.creationMessage;
import static com.server.bookstoremanager.user.util.MessageUtils.updatedMessage;

@Service
public class UserService {

    public static final UserMapper userMapper = UserMapper.INSTANCE;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MessageDTO create(UserDTO userDTO) {
        verifyIfExists(userDTO.getEmail(), userDTO.getUsername());
        User userToCreate = userMapper.toModel(userDTO);
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        User createdUser = userRepository.save(userToCreate);
        return creationMessage(createdUser);
    }

    public MessageDTO update(Long id, UserDTO userDTO) {
        User foundUser = verifyAndGetIfExists(id);
        userDTO.setId(foundUser.getId());

        User userToUpdate = userMapper.toModel(userDTO);
        userToUpdate.setCreatedDate(foundUser.getCreatedDate());
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        User updatedUser = userRepository.save(userToUpdate);
        return updatedMessage(updatedUser);
    }

    public void delete(Long id) {
        verifyAndGetIfExists(id);
        userRepository.deleteById(id);
    }

    public User verifyAndGetUserIfExists(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private void verifyIfExists(String email, String username) {
        Optional<User> foundUser = userRepository.findByEmailOrUsername(email, username);
        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException(email, username);
        }
    }

    private User verifyAndGetIfExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
