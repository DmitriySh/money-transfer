package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User post")
public class PostWithIdDto extends PostWithoutIdDto {
    @Schema(description = "Id of the post", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID postId;
}
