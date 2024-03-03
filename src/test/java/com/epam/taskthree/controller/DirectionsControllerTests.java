package com.epam.taskthree.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DirectionsController.class)
class DirectionsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetDirections() throws Exception {
        // Mock data
        String origin = "New York";
        String response = "{\"status\":\"OK\",\"routes\":[]}"; // Mock response from Directions API
        when(restTemplate.getForObject(anyString(), any())).thenReturn(response);

        // Perform GET request and verify response
        mockMvc.perform(get("/directions")
                        .with(csrf())
                        .param("origin", origin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response));

        // Verify that restTemplate.getForObject is called with correct URL
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }
}