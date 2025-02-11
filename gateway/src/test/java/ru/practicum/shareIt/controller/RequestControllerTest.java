package ru.practicum.shareIt.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.client.RequestClient;
import ru.practicum.shareit.itemRequest.controller.RequestController;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestClient requestClient;

    @Test
    public void testCreateRequest() throws Exception {
        // Подготовка данных
        String requestBody = "{\"description\":\"Need a drill\"}";
        Mockito.when(requestClient.createRequest(anyLong(), any()))
                .thenReturn("{\"id\":1,\"description\":\"Need a drill\"}");

        // Вызов эндпоинта и проверка результата
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need a drill"));
    }

    @Test
    public void testGetUserRequests() throws Exception {
        // Подготовка данных
        Mockito.when(requestClient.getUserRequests(anyLong()))
                .thenReturn("[{\"id\":1,\"description\":\"Need a drill\"}]");

        // Вызов эндпоинта и проверка результата
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Need a drill"));
    }

    @Test
    public void testGetAllRequests() throws Exception {
        // Подготовка данных
        Mockito.when(requestClient.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn("[{\"id\":1,\"description\":\"Need a drill\"}]");

        // Вызов эндпоинта и проверка результата
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Need a drill"));
    }

    @Test
    public void testGetRequestById() throws Exception {
        // Подготовка данных
        Mockito.when(requestClient.getRequestById(anyLong(), anyLong()))
                .thenReturn("{\"id\":1,\"description\":\"Need a drill\"}");

        // Вызов эндпоинта и проверка результата
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need a drill"));
    }
}