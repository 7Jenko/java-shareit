package ru.practicum.shareIt.dtoTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = CommentDtoTest.class)
public class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    public void testSerialize() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "Great item!", 1L, 2L, "John Doe", LocalDateTime.now());

        assertThat(json.write(commentDto))
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathStringValue("$.text")
                .hasJsonPathNumberValue("$.itemId")
                .hasJsonPathNumberValue("$.authorId")
                .hasJsonPathStringValue("$.authorName")
                .hasJsonPathStringValue("$.created");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"text\":\"Great item!\",\"itemId\":1,\"authorId\":2,\"authorName\":\"John Doe\",\"created\":\"2023-10-01T12:00:00\"}";

        assertThat(json.parse(content))
                .isEqualTo(new CommentDto(1L, "Great item!", 1L, 2L, "John Doe", LocalDateTime.parse("2023-10-01T12:00:00")));
    }
}