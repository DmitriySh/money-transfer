package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "File type")
public enum FileType {
    IMAGE,
    OTHER
}
