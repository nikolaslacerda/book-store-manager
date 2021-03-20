package com.server.bookstoremanager.book.controller;

import com.server.bookstoremanager.book.builder.BookRequestBuilder;
import com.server.bookstoremanager.book.builder.BookResponseBuilder;
import com.server.bookstoremanager.book.dto.BookRequestDTO;
import com.server.bookstoremanager.book.dto.BookResponseDTO;
import com.server.bookstoremanager.book.service.BookService;
import com.server.bookstoremanager.user.dto.AuthenticatedUser;
import com.server.bookstoremanager.util.JsonConversionUtils;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private static final String BOOKS_API_URL_PATH = "/api/v1/books";

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private BookRequestBuilder bookRequestDTOBuilder;

    private BookResponseBuilder bookResponseDTOBuilder;

    @BeforeEach
    void setUp() {
        bookRequestDTOBuilder = BookRequestBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .addFilters()
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPostIsCalledThenCreatedStatusShouldBeReturned() throws Exception {
        //given
        BookRequestDTO expectedBookToCreateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedCreatedBookDTO = bookResponseDTOBuilder.buildBookResponseDTO();

        //when
        when(bookService.create(any(AuthenticatedUser.class), eq(expectedBookToCreateDTO))).thenReturn(expectedCreatedBookDTO);

        //then
        mockMvc.perform(post(BOOKS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(expectedBookToCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedCreatedBookDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedCreatedBookDTO.getName())))
                .andExpect(jsonPath("$.isbn", is(expectedCreatedBookDTO.getIsbn())));
    }

    @Test
    void whenPostIsCalledWithoutRequiredFieldsThenBadRequestShouldBeReturned() throws Exception {
        //when
        BookRequestDTO expectedBookToCreateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        expectedBookToCreateDTO.setIsbn(null);

        //then
        mockMvc.perform(post(BOOKS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(expectedBookToCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetIsCalledThenStatusOkShouldBeInformed() throws Exception {
        //given
        BookRequestDTO expectedBookToFindDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedFoundBookDTO = bookResponseDTOBuilder.buildBookResponseDTO();

        //when
        when(bookService.findByIdAndUser(any(AuthenticatedUser.class), eq(expectedBookToFindDTO.getId()))).thenReturn(expectedFoundBookDTO);

        //then
        mockMvc.perform(get(BOOKS_API_URL_PATH + "/" + expectedBookToFindDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedFoundBookDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedFoundBookDTO.getName())))
                .andExpect(jsonPath("$.isbn", is(expectedFoundBookDTO.getIsbn())));
    }

    @Test
    void whenGetListIsCalledThenStatusOkShouldBeInformed() throws Exception {
        //given
        BookRequestDTO expectedBookToFind = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedCreatedBookDTO = bookResponseDTOBuilder.buildBookResponseDTO();

        //when
        when(bookService.findAllByUser(any(AuthenticatedUser.class)))
                .thenReturn(Collections.singletonList(expectedCreatedBookDTO));

        //then
        mockMvc.perform(get(BOOKS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(expectedBookToFind.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedBookToFind.getName())))
                .andExpect(jsonPath("$[0].isbn", is(expectedBookToFind.getIsbn())));
    }
}
