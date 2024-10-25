package ru.shishmakov.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
//@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User subscriptions response")
public class UserSubscriptionsResponse {
    @Schema(description = "Subscription id between users", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID subscriptionId;
    @Schema(description = "User id", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID userId;
    @Schema(description = "User who is subscribed to", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID followingUserId;
}
