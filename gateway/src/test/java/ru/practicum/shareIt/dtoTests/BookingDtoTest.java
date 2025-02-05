package ru.practicum.shareIt.dtoTests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

@JsonTest
public class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    public void testSerialize() throws Exception {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, null, null);

        assertThat(json.write(bookingDto))
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathStringValue("$.start")
                .hasJsonPathStringValue("$.end");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"start\":\"2023-10-01T12:00:00\",\"end\":\"2023-10-02T12:00:00\"}";

        assertThat(json.parse(content))
                .isEqualTo(new BookingDto(1L, LocalDateTime.parse("2023-10-01T12:00:00"), LocalDateTime.parse("2023-10-02T12:00:00"), null, null, null));
    }
}