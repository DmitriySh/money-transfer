package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Reaction")
public class PostCommentResponse {
    @Schema(description = "Id of the comment", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID commentId;
    @Schema(description = "Id of the post", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID postId;
    @Schema(description = "ID of the author who wrote the comment", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID commentUserId;
    @Schema(description = "Comment creation time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;
    @Schema(description = "Comment update time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant updatedAt;
}
