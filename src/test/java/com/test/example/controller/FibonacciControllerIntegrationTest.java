package com.test.example.controller;

import com.test.example.fibonacci.FibonacciController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
public class FibonacciControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FibonacciController fibonacciController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(fibonacciController).isNotNull();
    }

    @Test
    public void givenSeedAndVersionClassic_GetFibonacci_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/fibonacci")
                        .param("seed", "20")
                        .param("version","classic"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("6765"));
    }

    @Test
    public void givenSeedAndVersionCache_GetFibonacci_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/fibonacci")
                        .param("seed", "100")
                        .param("version","cache"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("354224848179261915075"));
    }

    @Test
    public void givenSeedAndVersionIterative_GetFibonacci_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/fibonacci")
                        .param("seed", "100")
                        .param("version","iterative"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("354224848179261915075"));
    }

    @Test
    public void givenSeedAndVersionIterativeString_GetFibonacci_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/fibonacci")
                        .param("seed", "100")
                        .param("version","iterative-string"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("354224848179261915075"));
    }

    @Test
    public void givenSeedAndVersionConcurrent_GetFibonacci_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/fibonacci")
                        .param("seed", "100")
                        .param("version","concurrent"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("354224848179261915075"));
    }
}
