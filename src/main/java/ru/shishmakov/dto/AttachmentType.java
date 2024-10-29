package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Attachment type")
public enum AttachmentType {
    IMAGE,
    OTHER
}
