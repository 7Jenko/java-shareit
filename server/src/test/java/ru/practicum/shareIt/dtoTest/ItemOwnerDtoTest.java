package ru.practicum.shareIt.dtoTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.item.dto.ItemOwnerDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ItemOwnerDtoTest.class)
public class ItemOwnerDtoTest {

    @Autowired
    private JacksonTester<ItemOwnerDto> json;

    @Test
    public void testSerialize() throws Exception {
        ItemOwnerDto itemOwnerDto = ItemOwnerDto.builder()
                .id(1L)
                .name("Drill")
                .description("Powerful drill")
                .available(true)
                .lastBooking(LocalDateTime.now())
                .nextBooking(LocalDateTime.now().plusDays(1))
                .build();

        assertThat(json.write(itemOwnerDto))
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathStringValue("$.name")
                .hasJsonPathStringValue("$.description")
                .hasJsonPathBooleanValue("$.available")
                .hasJsonPathStringValue("$.lastBooking")
                .hasJsonPathStringValue("$.nextBooking");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"Drill\",\"description\":\"Powerful drill\",\"available\":true,\"lastBooking\":\"2023-10-01T12:00:00\",\"nextBooking\":\"2023-10-02T12:00:00\"}";

        assertThat(json.parse(content))
                .isEqualTo(ItemOwnerDto.builder()
                        .id(1L)
                        .name("Drill")
                        .description("Powerful drill")
                        .available(true)
                        .lastBooking(LocalDateTime.parse("2023-10-01T12:00:00"))
                        .nextBooking(LocalDateTime.parse("2023-10-02T12:00:00"))
                        .build());
    }
}