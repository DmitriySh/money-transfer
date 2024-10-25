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
@Schema(description = "Attachment")
public class AttachmentDto {
    @Schema(description = "Attachment file id", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID attachmentId;
    @Schema(description = "File content type", requiredMode = Schema.RequiredMode.REQUIRED)
    private FileType contentType;
    @Schema(description = "Attachment file name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;
    @Schema(description = "The URL of the attachment file", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;
}
