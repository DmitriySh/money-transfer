package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Short post comment info")
public class PostCommentShortResponse {
    @Schema(description = "Id of the comment", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID commentId;
    @Schema(description = "Id of the post", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID postId;
}
