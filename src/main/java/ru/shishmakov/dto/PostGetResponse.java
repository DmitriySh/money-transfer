package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Full post info")
public class PostGetResponse extends PostWithIdDto {
    @Schema(description = "Id of the post author", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID postUserId;
    @Schema(description = "Post creation time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;
    @Schema(description = "Post update time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant updatedAt;
}
