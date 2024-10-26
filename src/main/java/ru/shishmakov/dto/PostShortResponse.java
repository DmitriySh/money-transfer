package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Short post info")
public class PostShortResponse {
    @Schema(description = "Id of the post author", requiredMode = RequiredMode.REQUIRED)
    private UUID postUserId;
    @Schema(description = "Id of the post", requiredMode = RequiredMode.REQUIRED)
    private UUID postId;
}
