package com.server.bookstoremanager.user.service;

import com.server.bookstoremanager.user.builder.JwtRequestBuilder;
import com.server.bookstoremanager.user.builder.UserBuilder;
import com.server.bookstoremanager.user.dto.JwtRequest;
import com.server.bookstoremanager.user.dto.JwtResponse;
import com.server.bookstoremanager.user.dto.UserDTO;
import com.server.bookstoremanager.user.entity.User;
import com.server.bookstoremanager.user.mapper.UserMapper;
import com.server.bookstoremanager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenManager jwtTokenManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserBuilder userBuilder;

    private JwtRequestBuilder jwtRequestBuilder;

    @BeforeEach
    void setUp() {
        userBuilder = UserBuilder.builder().build();
        jwtRequestBuilder = JwtRequestBuilder.builder().build();
    }

    @Test
    void whenUsernameAndPasswordIsInformedThenATokenShouldBeGenerated() {
        //given
        JwtRequest jwtRequest = jwtRequestBuilder.buildJwtRequest();
        UserDTO expectedFoundUserDTO = userBuilder.buildUserDTO();
        User expectedFoundUser = userMapper.toModel(expectedFoundUserDTO);
        String expectedGeneratedToken = "fakeToken";

        //when
        when(userRepository.findByUsername(jwtRequest.getUsername())).thenReturn(Optional.of(expectedFoundUser));
        when(jwtTokenManager.generateToken(any(UserDetails.class))).thenReturn(expectedGeneratedToken);
        JwtResponse generatedTokenResponse = authenticationService.createAuthenticationToken(jwtRequest);

        //then
        assertThat(generatedTokenResponse.getJwtToken(), is(equalTo(expectedGeneratedToken)));
    }

    @Test
    void whenUsernameIsInformedThenUserShouldBeReturned() {
        //given
        UserDTO expectedFoundUserDTO = userBuilder.buildUserDTO();
        User expectedFoundUser = userMapper.toModel(expectedFoundUserDTO);
        String expectedUsername = expectedFoundUserDTO.getUsername();
        SimpleGrantedAuthority expectedUserRole = new SimpleGrantedAuthority("ROLE_" + expectedFoundUserDTO.getRole().getDescription());

        //when
        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.of(expectedFoundUser));
        UserDetails userDetails = authenticationService.loadUserByUsername(expectedUsername);

        //then
        assertThat(userDetails.getUsername(), is(equalTo(expectedFoundUser.getUsername())));
        assertThat(userDetails.getPassword(), is(equalTo(expectedFoundUser.getPassword())));
        assertTrue(userDetails.getAuthorities().contains(expectedUserRole));
    }

    @Test
    void whenInvalidUsernameIsInformedThenAnExceptionShouldBeThrown() {
        //given
        UserDTO expectedFoundUserDTO = userBuilder.buildUserDTO();
        String expectedUsername = expectedFoundUserDTO.getUsername();

        //when
        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.empty());

        //then
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername(expectedUsername));
    }
}
