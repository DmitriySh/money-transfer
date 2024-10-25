package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Create a new post comment")
public class PostCommentCreateRequest {
    @Schema(description = "Comment text", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comment;
}
