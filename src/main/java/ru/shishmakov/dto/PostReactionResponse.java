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
@Schema(description = "Post reaction response")
public class PostReactionResponse {
    @Schema(description = "Id of the reaction", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID reactionId;
    @Schema(description = "Id of the post", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID postId;
    @Schema(description = "ID of the author who placed the reaction", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID reactionUserId;
    @Schema(description = "Reaction type", requiredMode = Schema.RequiredMode.REQUIRED)
    private ReactionType reactionType;
    @Schema(description = "Reaction creation time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;
}
