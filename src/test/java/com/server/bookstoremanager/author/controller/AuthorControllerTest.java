package com.server.bookstoremanager.author.controller;

import com.server.bookstoremanager.author.builder.AuthorBuilder;
import com.server.bookstoremanager.author.dto.AuthorDTO;
import com.server.bookstoremanager.author.service.AuthorService;
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

import java.util.Collections;

import static com.server.bookstoremanager.util.JsonConversionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {

    private static final String AUTHOR_API_URL_PATH = "/api/v1/authors";

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;

    private AuthorBuilder authorBuilder;

    @BeforeEach
    void setUp() {
        authorBuilder = AuthorBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPostIsCalledThenStatusCreatedShouldBeReturned() throws Exception {
        //given
        AuthorDTO expectedCreatedAuthorDTO = authorBuilder.buildAuthorDTO();

        //when
        when(authorService.create(expectedCreatedAuthorDTO)).thenReturn(expectedCreatedAuthorDTO);

        //then
        mockMvc.perform(post(AUTHOR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedCreatedAuthorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedCreatedAuthorDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedCreatedAuthorDTO.getName())))
                .andExpect(jsonPath("$.age", is(expectedCreatedAuthorDTO.getAge())));
    }

    @Test
    void whenPostIsCalledWithoutRequiredFieldsThenBadRequestShouldBeInformed() throws Exception {
        //given
        AuthorDTO expectedCreatedAuthorDTO = authorBuilder.buildAuthorDTO();
        expectedCreatedAuthorDTO.setName(null);

        //then
        mockMvc.perform(post(AUTHOR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedCreatedAuthorDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetWithValidIdIsCalledThenStatusOkShouldBeReturned() throws Exception {
        //given
        AuthorDTO expectedFoundAuthorDTO = authorBuilder.buildAuthorDTO();

        //when
        when(authorService.findById(expectedFoundAuthorDTO.getId())).thenReturn(expectedFoundAuthorDTO);

        //then
        mockMvc.perform(get(AUTHOR_API_URL_PATH + "/" + expectedFoundAuthorDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedFoundAuthorDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedFoundAuthorDTO.getName())))
                .andExpect(jsonPath("$.age", is(expectedFoundAuthorDTO.getAge())));
    }

    @Test
    void whenGetListIsCalledThenStatusOkShouldBeReturned() throws Exception {
        //given
        AuthorDTO expectedFoundAuthorDTO = authorBuilder.buildAuthorDTO();

        //when
        when(authorService.findAll()).thenReturn(Collections.singletonList(expectedFoundAuthorDTO));

        //then
        mockMvc.perform(get(AUTHOR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(expectedFoundAuthorDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedFoundAuthorDTO.getName())))
                .andExpect(jsonPath("$[0].age", is(expectedFoundAuthorDTO.getAge())));
    }

    @Test
    void whenDeleteWithValidIdIsCalledThenNoContentShouldBeReturned() throws Exception {
        //given
        AuthorDTO expectedAuthorDeletedDTO = authorBuilder.buildAuthorDTO();
        Long expectedAuthorDeletedId = expectedAuthorDeletedDTO.getId();

        //when
        doNothing().when(authorService).delete(expectedAuthorDeletedId);

        //then
        mockMvc.perform(delete(AUTHOR_API_URL_PATH + "/" + expectedAuthorDeletedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
