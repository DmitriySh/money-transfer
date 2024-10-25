package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Reaction type")
public enum ReactionType {
    LIKE,
    DISLIKE
}
