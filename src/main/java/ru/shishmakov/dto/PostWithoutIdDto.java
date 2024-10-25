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
@Schema(description = "User post")
public class PostWithoutIdDto {
    @Schema(description = "The content of the post", requiredMode = Schema.RequiredMode.REQUIRED)
    private String text;
    @Schema(
            description = "The location x-coordinate of the place associated with this post",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "0.0"
    )
    double coordinateX;
    @Schema(
            description = "The location y-coordinate of the place associated with this post",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "0.1"
    )
    double coordinateY;
}
