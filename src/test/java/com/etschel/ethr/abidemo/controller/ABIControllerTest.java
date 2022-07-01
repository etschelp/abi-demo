package com.etschel.ethr.abidemo.controller;

import com.etschel.ethr.abidemo.controller.api.PersistABIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ABIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testPostEmptyABI() throws Exception{
        String content = mockMvc
                .perform(
                        post("/abi")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        PersistABIResponse persistABIResponse = mapper.readValue(content, PersistABIResponse.class);
        Assertions.assertNotNull(persistABIResponse.getId());
    }
}
