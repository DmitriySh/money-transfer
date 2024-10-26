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
@Schema(description = "User info response")
public class UserResponse {
    @Schema(description = "User id", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID userId;
    @Schema(description = "User full name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;
    @Schema(description = "Brief information about the user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;
    @Schema(description = "User profile icon", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String iconUrl;
    @Schema(description = "User email", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(description = "User creation time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;
}
