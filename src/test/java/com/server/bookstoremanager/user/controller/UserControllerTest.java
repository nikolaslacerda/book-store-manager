package com.server.bookstoremanager.user.controller;

import com.server.bookstoremanager.user.builder.JwtRequestBuilder;
import com.server.bookstoremanager.user.builder.UserBuilder;
import com.server.bookstoremanager.user.dto.JwtRequest;
import com.server.bookstoremanager.user.dto.JwtResponse;
import com.server.bookstoremanager.user.dto.MessageDTO;
import com.server.bookstoremanager.user.dto.UserDTO;
import com.server.bookstoremanager.user.service.AuthenticationService;
import com.server.bookstoremanager.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.server.bookstoremanager.util.JsonConversionUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final String USERS_API_URL_PATH = "/api/v1/users";

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserController userController;

    private UserBuilder userBuilder;

    private JwtRequestBuilder jwtRequestBuilder;

    @BeforeEach
    void setUp() {
        userBuilder = UserBuilder.builder().build();
        jwtRequestBuilder = JwtRequestBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPostIsCalledThenCreatedStatusShouldBeReturned() throws Exception {
        //given
        UserDTO expectedUserToCreatedDTO = userBuilder.buildUserDTO();
        String expectedCreationMessage = "User nikolas with id 1 successfully created";
        MessageDTO expectedCreationMessageDTO = MessageDTO.builder().message(expectedCreationMessage).build();

        //when
        when(userService.create(expectedUserToCreatedDTO)).thenReturn(expectedCreationMessageDTO);

        //then
        mockMvc.perform(post(USERS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedUserToCreatedDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(expectedCreationMessage)));
    }

    @Test
    void whenPostIsCalledWithoutRequiredFieldThenBadRequestStatusShouldBeReturned() throws Exception {
        //given
        UserDTO expectedUserToCreatedDTO = userBuilder.buildUserDTO();
        expectedUserToCreatedDTO.setUsername(null);

        //then
        mockMvc.perform(post(USERS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedUserToCreatedDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPutIsCalledThenOkStatusShouldBeReturned() throws Exception {
        //given
        UserDTO expectedUserToUpdatedDTO = userBuilder.buildUserDTO();
        expectedUserToUpdatedDTO.setUsername("nikolasupdate");
        Long expectedUserToUpdateId = expectedUserToUpdatedDTO.getId();
        String expectedUpdateMessage = "User nikolasupdate with id 1 successfully updated";
        MessageDTO expectedUpdatedMessageDTO = MessageDTO.builder().message(expectedUpdateMessage).build();

        //when
        when(userService.update(expectedUserToUpdateId, expectedUserToUpdatedDTO)).thenReturn(expectedUpdatedMessageDTO);

        //then
        mockMvc.perform(put(USERS_API_URL_PATH + "/" + expectedUserToUpdateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedUserToUpdatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(expectedUpdateMessage)));
    }

    @Test
    void whenDeleteIsCalledThenNoContentStatusShouldBeInformed() throws Exception {
        //given
        UserDTO expectedUserToDeleteDTO = userBuilder.buildUserDTO();
        Long expectedUserToDeleteId = expectedUserToDeleteDTO.getId();

        //when
        doNothing().when(userService).delete(expectedUserToDeleteId);

        //then
        mockMvc.perform(delete(USERS_API_URL_PATH + "/" + expectedUserToDeleteId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenPostToAuthenticateUserThenOkStatusShouldBeReturned() throws Exception {
        //given
        JwtRequest jwtRequest = jwtRequestBuilder.buildJwtRequest();
        JwtResponse expectedJwtToken = JwtResponse.builder().jwtToken("fakeToken").build();

        //when
        when(authenticationService.createAuthenticationToken(jwtRequest)).thenReturn(expectedJwtToken);

        //then
        mockMvc.perform(post(USERS_API_URL_PATH + "/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(jwtRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken", is(expectedJwtToken.getJwtToken())));
    }

    @Test
    void whenPostToAuthenticateUserWithoutPasswordThenBadRequestShouldBeReturned() throws Exception {
        //given
        JwtRequest jwtRequest = jwtRequestBuilder.buildJwtRequest();
        jwtRequest.setPassword(null);

        //then
        mockMvc.perform(post(USERS_API_URL_PATH + "/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(jwtRequest)))
                .andExpect(status().isBadRequest());
    }
}
