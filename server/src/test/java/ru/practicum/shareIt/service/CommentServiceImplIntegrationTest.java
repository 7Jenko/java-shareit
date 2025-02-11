package ru.practicum.shareIt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.service.CommentServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {ShareItServer.class})
@Transactional
@Rollback
public class CommentServiceImplIntegrationTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void testAddComment() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        UserDto createdUser = userService.createUser(userDto);

        UserDto ownerDto = new UserDto();
        ownerDto.setName("Owner");
        ownerDto.setEmail("owner@example.com");
        UserDto createdOwner = userService.createUser(ownerDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item 1");
        itemDto.setDescription("Description 1");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.createItem(createdOwner.getId(), itemDto);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(createdItem.getId());
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(1).toString());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(2).toString());
        BookingDto createdBooking = bookingService.createBooking(createdUser.getId(), bookingRequestDto);

        Booking booking = bookingRepository.findById(createdBooking.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        bookingRepository.save(booking);

        CommentDto commentDto = commentService.addComment(createdUser.getId(), createdItem.getId(), "Great item!");

        assertNotNull(commentDto.getId());
        assertEquals("Great item!", commentDto.getText());
    }
}