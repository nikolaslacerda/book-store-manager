package com.server.bookstoremanager.user.service;

import com.server.bookstoremanager.user.builder.UserBuilder;
import com.server.bookstoremanager.user.dto.MessageDTO;
import com.server.bookstoremanager.user.dto.UserDTO;
import com.server.bookstoremanager.user.entity.User;
import com.server.bookstoremanager.user.exception.UserAlreadyExistsException;
import com.server.bookstoremanager.user.exception.UserNotFoundException;
import com.server.bookstoremanager.user.mapper.UserMapper;
import com.server.bookstoremanager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserBuilder userBuilder;

    @BeforeEach
    void setUp() {
        userBuilder = UserBuilder.builder().build();
    }

    @Test
    void whenNewUserIsInformedThenItShouldBeCreated() {
        //given
        UserDTO expectedCreatedUserDTO = userBuilder.buildUserDTO();
        User expectedCreatedUser = userMapper.toModel(expectedCreatedUserDTO);
        String expectedCreationMessage = "User nikolas with id 1 successfully created";
        String expectedUserEmail = expectedCreatedUserDTO.getEmail();
        String expectedUsername = expectedCreatedUserDTO.getUsername();

        //when
        when(userRepository.findByEmailOrUsername(expectedUserEmail, expectedUsername)).thenReturn(Optional.empty());
        when(userRepository.save(expectedCreatedUser)).thenReturn(expectedCreatedUser);
        MessageDTO creationMessage = userService.create(expectedCreatedUserDTO);

        //then
        assertThat(expectedCreationMessage, is(equalTo(creationMessage.getMessage())));
    }

    @Test
    void whenExistingUserIsInformedThenAnExceptionShouldBeThrown() {
        //given
        UserDTO expectedDuplicatedUserDTO = userBuilder.buildUserDTO();
        User expectedDuplicatedUser = userMapper.toModel(expectedDuplicatedUserDTO);
        String expectedUserEmail = expectedDuplicatedUserDTO.getEmail();
        String expectedUsername = expectedDuplicatedUserDTO.getUsername();

        //when
        when(userRepository.findByEmailOrUsername(expectedUserEmail, expectedUsername))
                .thenReturn(Optional.of(expectedDuplicatedUser));

        //then
        assertThrows(UserAlreadyExistsException.class, () -> userService.create(expectedDuplicatedUserDTO));
    }

    @Test
    void whenValidUserIsInformedThenItShouldBeDeleted() {
        //given
        UserDTO expectedDeletedUserDTO = userBuilder.buildUserDTO();
        User expectedDeletedUser = userMapper.toModel(expectedDeletedUserDTO);
        Long expectedDeletedUserId = expectedDeletedUserDTO.getId();

        //when
        when(userRepository.findById(expectedDeletedUserId)).thenReturn(Optional.of(expectedDeletedUser));
        doNothing().when(userRepository).deleteById(expectedDeletedUserId);
        userService.delete(expectedDeletedUserId);

        //then
        verify(userRepository, times(1)).deleteById(expectedDeletedUserId);
    }

    @Test
    void whenInvalidUserIdIsInformedThenAnExceptionShouldBeThrown() {
        //given
        UserDTO expectedDeletedUserDTO = userBuilder.buildUserDTO();
        Long expectedDeletedUserId = expectedDeletedUserDTO.getId();

        //when
        when(userRepository.findById(expectedDeletedUserId)).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class, () -> userService.delete(expectedDeletedUserId));
    }
}
