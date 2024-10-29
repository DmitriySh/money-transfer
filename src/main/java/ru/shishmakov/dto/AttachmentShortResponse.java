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
@Schema(description = "Attachment short response")
public class AttachmentShortResponse {
    @Schema(description = "Id of the resource", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private UUID resourceId;
    @Schema(description = "Id of the attachment file", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID attachmentId;
    @Schema(description = "Attachment file name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;
    @Schema(description = "Attachment file URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;
}
