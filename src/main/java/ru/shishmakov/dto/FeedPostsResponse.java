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
@Schema(description = "Post's feed response")
public class FeedPostsResponse {
    @Schema(description = "Post's feed", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PostGetResponse> posts;
}
