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
@Schema(description = "Create a new post reaction")
public class PostReactionRequest {
    @Schema(description = "ID of the author who placed the reaction", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID reactionUserId;
    @Schema(description = "Reaction type", requiredMode = Schema.RequiredMode.REQUIRED)
    private ReactionType reactionType;

}
