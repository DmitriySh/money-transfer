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
@Schema(description = "Attachment response")
public class AttachmentResponse extends AttachmentShortResponse {
    @Schema(description = "Resource type that the attachment belongs to", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ResourceType resourceType;
    @Schema(description = "Id of the attachment author", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID userId;
    @Schema(description = "Type of attachment file", requiredMode = Schema.RequiredMode.REQUIRED)
    private AttachmentType attachmentType;
    @Schema(description = "Attachment creation time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;
}
