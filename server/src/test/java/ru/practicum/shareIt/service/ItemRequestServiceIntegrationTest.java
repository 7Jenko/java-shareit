package ru.practicum.shareIt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.dto.ItemRequestResponseDto;
import ru.practicum.shareit.itemRequest.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {ShareItServer.class})
@Transactional
@Rollback
public class ItemRequestServiceIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateRequest() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        UserDto createdUser = userService.createUser(userDto);

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");

        ItemRequestResponseDto response = itemRequestService.createRequest(createdUser.getId(), requestDto);

        assertNotNull(response.getId());
        assertEquals("Need a drill", response.getDescription());
        assertNotNull(response.getCreated());
    }

    @Test
    public void testGetUserRequests() {
        // Подготовка данных
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        UserDto createdUser = userService.createUser(userDto);

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        itemRequestService.createRequest(createdUser.getId(), requestDto);

        List<ItemRequestResponseDto> requests = itemRequestService.getUserRequests(createdUser.getId());

        assertEquals(1, requests.size());
        assertEquals("Need a drill", requests.get(0).getDescription());
    }

    @Test
    public void testGetAllRequests() {
        // Подготовка данных
        UserDto userDto1 = new UserDto();
        userDto1.setName("John Doe");
        userDto1.setEmail("john@example.com");
        UserDto createdUser1 = userService.createUser(userDto1);

        UserDto userDto2 = new UserDto();
        userDto2.setName("Jane Doe");
        userDto2.setEmail("jane@example.com");
        UserDto createdUser2 = userService.createUser(userDto2);

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        itemRequestService.createRequest(createdUser1.getId(), requestDto);

        List<ItemRequestResponseDto> requests = itemRequestService.getAllRequests(createdUser2.getId());

        assertEquals(1, requests.size());
        assertEquals("Need a drill", requests.get(0).getDescription());
    }

    @Test
    public void testGetRequestById() {
        // Подготовка данных
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        UserDto createdUser = userService.createUser(userDto);

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        ItemRequestResponseDto createdRequest = itemRequestService.createRequest(createdUser.getId(), requestDto);

        ItemRequestResponseDto response = itemRequestService.getRequestById(createdUser.getId(), createdRequest.getId());

        assertEquals("Need a drill", response.getDescription());
        assertNotNull(response.getCreated());
    }
}