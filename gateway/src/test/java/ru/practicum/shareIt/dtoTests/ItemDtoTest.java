package ru.practicum.shareIt.dtoTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Set;

@JsonTest
@ContextConfiguration(classes = ItemDtoTest.class)
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    public void testSerialize() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Drill", "Powerful drill", true, null, null);

        assertThat(json.write(itemDto))
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathStringValue("$.name")
                .hasJsonPathStringValue("$.description")
                .hasJsonPathBooleanValue("$.available");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"Drill\",\"description\":\"Powerful drill\",\"available\":true}";

        assertThat(json.parse(content))
                .isEqualTo(new ItemDto(1L, "Drill", "Powerful drill", true, null, null));
    }

    @Test
    public void testValidation() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("");
        itemDto.setDescription("");
        itemDto.setAvailable(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
    }
}