package com.server.bookstoremanager.publisher.controller;

import com.server.bookstoremanager.publisher.builder.PublisherBuilder;
import com.server.bookstoremanager.publisher.dto.PublisherDTO;
import com.server.bookstoremanager.publisher.service.PublisherService;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PublisherControllerTest {

    public static final String PUBLISHERS_API_URL_PATH = "/api/v1/publishers";

    private MockMvc mockMvc;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private PublisherController publisherController;

    private PublisherBuilder publisherBuilder;

    @BeforeEach
    void setUp() {
        publisherBuilder = PublisherBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(publisherController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPostIsCalledThenCreatedStatusShouldBeReturned() throws Exception {
        //given
        PublisherDTO expectedCreatedPublisherDTO = publisherBuilder.buildPublisherDTO();

        //when
        when(publisherService.create(expectedCreatedPublisherDTO)).thenReturn(expectedCreatedPublisherDTO);

        //then
        mockMvc.perform(post(PUBLISHERS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedCreatedPublisherDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedCreatedPublisherDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedCreatedPublisherDTO.getName())))
                .andExpect(jsonPath("$.code", is(expectedCreatedPublisherDTO.getCode())));
    }

    @Test
    void whenPostIsCalledWithoutRequiredFieldsThenBadRequestStatusShouldBeInformed() throws Exception {
        //given
        PublisherDTO expectedCreatedPublisherDTO = publisherBuilder.buildPublisherDTO();
        expectedCreatedPublisherDTO.setName(null);

        //then
        mockMvc.perform(post(PUBLISHERS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedCreatedPublisherDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetWithValidIdIsCalledThenOkStatusShouldBeInformed() throws Exception {
        //given
        PublisherDTO expectedCreatedPublisherDTO = publisherBuilder.buildPublisherDTO();
        Long expectedCreatedPublisherDTOId = expectedCreatedPublisherDTO.getId();

        //when
        when(publisherService.findById(expectedCreatedPublisherDTOId)).thenReturn(expectedCreatedPublisherDTO);

        //then
        mockMvc.perform(get(PUBLISHERS_API_URL_PATH + "/" + expectedCreatedPublisherDTOId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedCreatedPublisherDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedCreatedPublisherDTOId.intValue())))
                .andExpect(jsonPath("$.name", is(expectedCreatedPublisherDTO.getName())))
                .andExpect(jsonPath("$.code", is(expectedCreatedPublisherDTO.getCode())));
    }

    @Test
    void whenGetListIsCalledThenOkStatusShouldBeInformed() throws Exception {
        //given
        PublisherDTO expectedCreatedPublisherDTO = publisherBuilder.buildPublisherDTO();

        //when
        when(publisherService.findAll()).thenReturn(Collections.singletonList(expectedCreatedPublisherDTO));

        //then
        mockMvc.perform(get(PUBLISHERS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedCreatedPublisherDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(expectedCreatedPublisherDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedCreatedPublisherDTO.getName())))
                .andExpect(jsonPath("$[0].code", is(expectedCreatedPublisherDTO.getCode())));
    }

    @Test
    void whenDeleteIsCalledThenNoContentStatusShouldBeInformed() throws Exception {
        //given
        PublisherDTO expectedPublisherToDeleteDTO = publisherBuilder.buildPublisherDTO();
        Long expectedPublisherToDeleteId = expectedPublisherToDeleteDTO.getId();

        //when
        doNothing().when(publisherService).delete(expectedPublisherToDeleteId);

        //then
        mockMvc.perform(delete(PUBLISHERS_API_URL_PATH + "/" + expectedPublisherToDeleteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedPublisherToDeleteDTO)))
                .andExpect(status().isNoContent());
    }
}
