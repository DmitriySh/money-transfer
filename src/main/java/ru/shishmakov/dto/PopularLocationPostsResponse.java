package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Popular posts by coordinates location")
public class PopularLocationPostsResponse {
    @Schema(description = "Popular posts", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PostGetResponse> posts;
}
