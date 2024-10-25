package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Post attachment response")
public class PostAttachmentResponse {
    @Schema(description = "Id of the post", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID postId;
    @Schema(description = "Id of the post author", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID postUserId;
    @Schema(description = "Attachment file id", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID attachmentId;
    @Schema(description = "File content type", requiredMode = Schema.RequiredMode.REQUIRED)
    private FileType contentType;
    @Schema(description = "Attachment file name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;
    @Schema(description = "Attachment file URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;
    @Schema(description = "Attachment creation time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;
}
