package ru.practicum.shareIt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {ShareItServer.class})
@Transactional
@Rollback
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser.getId());
        assertEquals("John Doe", createdUser.getName());
        assertEquals("john@example.com", createdUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        UserDto createdUser = userService.createUser(userDto);

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setName("Jane Doe");
        updatedUserDto.setEmail("jane@example.com");

        UserDto updatedUser = userService.updateUser(createdUser.getId(), updatedUserDto);

        assertEquals("Jane Doe", updatedUser.getName());
        assertEquals("jane@example.com", updatedUser.getEmail());
    }

    @Test
    public void testDeleteUser() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        UserDto createdUser = userService.createUser(userDto);

        userService.deleteUser(createdUser.getId());

        assertNull(userService.getUser(createdUser.getId()));
    }
}