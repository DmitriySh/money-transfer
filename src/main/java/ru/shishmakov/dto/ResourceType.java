package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resource type that the attachment belongs to")
public enum ResourceType {
    POST,
    OTHER
}
