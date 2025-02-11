package ru.practicum.shareIt.dtoTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@JsonTest
@ContextConfiguration(classes = UserDtoTest.class)
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    public void testSerialize() throws Exception {
        UserDto userDto = new UserDto(1L, "John Doe", "john@example.com");

        assertThat(json.write(userDto))
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathStringValue("$.name")
                .hasJsonPathStringValue("$.email");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}";

        assertThat(json.parse(content))
                .isEqualTo(new UserDto(1L, "John Doe", "john@example.com"));
    }

    @Test
    public void testValidation() {
        UserDto userDto = new UserDto();
        userDto.setName(null);
        userDto.setEmail("invalid-email");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }
}