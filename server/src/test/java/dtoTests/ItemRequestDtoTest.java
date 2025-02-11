package dtoTests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@JsonTest
@ContextConfiguration(classes = ItemRequestDtoTest.class)
public class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    public void testSerialize() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");

        assertThat(json.write(requestDto))
                .hasJsonPathStringValue("$.description")
                .extractingJsonPathStringValue("$.description").isEqualTo("Need a drill");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"description\":\"Need a drill\"}";

        assertThat(json.parse(content))
                .isEqualTo(new ItemRequestDto("Need a drill"));
    }

    @Test
    public void testValidation() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(requestDto);

        assertFalse(violations.isEmpty());
        assertEquals("Описание запроса не может быть пустым", violations.iterator().next().getMessage());
    }
}