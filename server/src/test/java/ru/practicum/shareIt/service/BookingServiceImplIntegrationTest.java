package ru.practicum.shareIt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ItemServiceImpl itemService;

    @Test
    public void testCreateBooking() {
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

        assertNotNull(createdBooking.getId());
        assertEquals(BookingStatus.WAITING, createdBooking.getStatus());
    }

    @Test
    public void testApproveBooking() {
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

        BookingDto approvedBooking = bookingService.approveBooking(createdOwner.getId(), createdBooking.getId(), true);

        assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
    }

    @Test
    public void testGetBookingsByUser() {
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
        bookingService.createBooking(createdUser.getId(), bookingRequestDto);

        List<BookingDto> bookings = bookingService.getBookingsByUser(createdUser.getId(), "ALL");

        assertFalse(bookings.isEmpty());
    }
}
