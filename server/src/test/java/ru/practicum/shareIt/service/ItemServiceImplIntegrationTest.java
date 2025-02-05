package ru.practicum.shareIt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void testCreateItem() {
        // Подготовка данных
        UserDto userDto = new UserDto();
        userDto.setName("Owner");
        userDto.setEmail("owner@example.com");
        UserDto createdUser = userService.createUser(userDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item 1");
        itemDto.setDescription("Description 1");
        itemDto.setAvailable(true);

        // Вызов метода
        ItemDto createdItem = itemService.createItem(createdUser.getId(), itemDto);

        // Проверка результата
        assertNotNull(createdItem.getId());
        assertEquals("Item 1", createdItem.getName());
        assertEquals("Description 1", createdItem.getDescription());
        assertTrue(createdItem.getAvailable());
    }

    @Test
    public void testUpdateItem() {
        UserDto userDto = new UserDto();
        userDto.setName("Owner");
        userDto.setEmail("owner@example.com");
        UserDto createdUser = userService.createUser(userDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item 1");
        itemDto.setDescription("Description 1");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.createItem(createdUser.getId(), itemDto);

        ItemDto updatedItemDto = new ItemDto();
        updatedItemDto.setName("Updated Item");
        updatedItemDto.setDescription("Updated Description");
        updatedItemDto.setAvailable(false);

        ItemDto updatedItem = itemService.updateItem(createdUser.getId(), createdItem.getId(), updatedItemDto);

        assertEquals("Updated Item", updatedItem.getName());
        assertEquals("Updated Description", updatedItem.getDescription());
        assertFalse(updatedItem.getAvailable());
    }
}