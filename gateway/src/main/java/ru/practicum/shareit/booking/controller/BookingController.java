package ru.practicum.shareit.booking.controller;

import exceptions.NotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

@Slf4j
@RestController
@RequestMapping("/bookings")
public class BookingController {

	private final BookingClient bookingClient;

	public BookingController(BookingClient bookingClient) {
		this.bookingClient = bookingClient;
	}

	@PostMapping
	public ResponseEntity<?> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
										   @Valid @RequestBody BookingRequestDto bookingRequestDto) {
		try {
			Object response = bookingClient.createBooking(userId, bookingRequestDto);
			return ResponseEntity.ok(response);
		} catch (NotFoundException e) {
			log.error("Item not found: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		} catch (RuntimeException e) {
			log.error("Server error: {}", e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}

	@PatchMapping("/{bookingId}")
	public Object approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
								 @PathVariable Long bookingId,
								 @RequestParam Boolean approved) {
		return bookingClient.approveBooking(userId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public Object getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
							 @PathVariable Long bookingId) {
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping
	public Object getBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
									@RequestParam(defaultValue = "ALL") String state) {
		return bookingClient.getBookingsByUser(userId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<?> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
												@RequestParam(defaultValue = "ALL") String state) {
		try {
			Object response = bookingClient.getBookingsByOwner(ownerId, state);
			return ResponseEntity.ok(response);
		} catch (NotFoundException e) {
			log.error("Bookings not found: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		} catch (RuntimeException e) {
			log.error("Server error: {}", e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}
}