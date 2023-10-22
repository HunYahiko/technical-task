package com.gringotts.technicaltask.web;

import com.gringotts.technicaltask.service.counter.SuccessfulRequestCounterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SuccessfulRequestCounterControllerTest {

    @Mock
    private SuccessfulRequestCounterService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        final var controller = new SuccessfulRequestCounterController(service);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getCountOfSuccessfulRequests_whenInvoked_returnsExpectedResult() throws Exception {
        when(service.getCurrentCount()).thenReturn(12);

        mockMvc.perform(get("/counter"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").value(12));
    }


}