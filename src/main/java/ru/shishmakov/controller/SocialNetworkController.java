package ru.shishmakov.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.shishmakov.dto.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(info = @Info(
        title = "Social network for travelers API",
        description = "Current REST API describes interaction with Social network",
        version = "1.0.0"
))
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SocialNetworkController {

    // ============= posts ============= //

    @Operation(
            summary = "New post",
            description = "User make a new post",
            tags = {"posts"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/posts", produces = "application/json", consumes = "application/json")
    public PostShortResponse createPost(
            @RequestBody PostCreateRequest postRequest
    ) {
        return PostShortResponse.builder()
                .postUserId(postRequest.getPostUserId())
                .postId(UUID.randomUUID())
                .build();
    }

    @Operation(
            summary = "Get feed of posts with pagination",
            description = "Get user's feed of posts with pagination",
            tags = {"posts"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/posts/user-feeds", produces = "application/json")
    public FeedPostsResponse getUserFeedPosts(
            @Parameter(description = "Id of the post author")
            @RequestParam UUID userId,
            @Parameter(description = "The limit of records")
            @RequestParam(required = false, defaultValue = "10") int limit,
            @Parameter(description = "The offset of records")
            @RequestParam(required = false, defaultValue = "0") int offset
    ) {
        var post = PostGetResponse.builder()
                .postUserId(UUID.randomUUID())
                .text("post text")
                .coordinateX(0.0)
                .coordinateY(0.0)
                .createdAt(Instant.now())
                .updatedAt(Instant.now().plus(3, ChronoUnit.MINUTES))
                .build();
        return FeedPostsResponse.builder()
                .posts(List.of(post))
                .build();
    }

    @Operation(
            summary = "Get popular posts with pagination",
            description = "Get popular users posts by coordinates location with pagination",
            tags = {"posts"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/posts/popular-locations", produces = "application/json")
    public PopularLocationPostsResponse getPopularLocationPosts(
            @Parameter(description = "The x-coordinate to determine the location")
            @RequestParam double coordinateX,
            @Parameter(description = "The y-coordinate to determine the location")
            @RequestParam double coordinateY,
            @Parameter(description = "Radius of distance from coordinate point")
            @RequestParam(defaultValue = "100") int radius,
            @Parameter(description = "The limit of records")
            @RequestParam(required = false, defaultValue = "10") int limit,
            @Parameter(description = "The offset of records")
            @RequestParam(required = false, defaultValue = "0") int offset
    ) {
        var post = PostGetResponse.builder()
                .postUserId(UUID.randomUUID())
                .text("post text")
                .coordinateX(0.0)
                .coordinateY(0.0)
                .createdAt(Instant.now())
                .updatedAt(Instant.now().plus(3, ChronoUnit.MINUTES))
                .build();
        return PopularLocationPostsResponse.builder()
                .posts(List.of(post))
                .build();
    }

    @Operation(
            summary = "Get post",
            description = "Get user post by id",
            tags = {"posts"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/posts/{postId}", produces = "application/json")
    public PostGetResponse getPostById(
            @Parameter(description = "Post id")
            @PathVariable UUID postId
    ) {
        return PostGetResponse.builder()
                .postUserId(UUID.randomUUID())
                .postId(postId)
                .text("post text")
                .coordinateX(0.0)
                .coordinateY(0.1)
                .createdAt(Instant.now())
                .updatedAt(Instant.now().plus(3, ChronoUnit.MINUTES))
                .build();
    }

    @Operation(
            summary = "Update post",
            description = "User update the post by id",
            tags = {"posts"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/posts/{postId}", produces = "application/json", consumes = "application/json")
    public PostShortResponse updatePost(
            @Parameter(description = "Post id")
            @PathVariable UUID postId,
            @RequestBody PostUpdateRequest postRequest
    ) {
        return PostShortResponse.builder()
                .postUserId(postRequest.getPostUserId())
                .postId(postId)
                .build();
    }

    @Operation(
            summary = "Delete post",
            description = "User delete post by id",
            tags = {"posts"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/posts/{postId}")
    public void deletePostById(
            @Parameter(description = "Post id")
            @PathVariable UUID postId
    ) {
        // delete post
    }


    // ============= attachments ============= //


    @Operation(
            summary = "New attachment",
            description = "User make a new attachment",
            tags = {"attachments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/attachments", produces = "application/json", consumes = "multipart/form-data")
    public AttachmentShortResponse uploadAttachment(
            @Parameter(description = "Resource id that the attachment belongs to")
            @RequestParam(required = false) UUID resourceId,
            @Parameter(description = "Resource type that the attachment belongs to", example = "POST")
            @RequestParam(required = false) ResourceType resourceType,
            @Parameter(description = "Attachment type", example = "IMAGE")
            @RequestParam AttachmentType attachmentType,
            @Parameter(description = "Attachment file content")
            @RequestParam("file") MultipartFile attachmentContent
    ) {
        return AttachmentShortResponse.builder()
                .resourceId(resourceId)
                .attachmentId(UUID.randomUUID())
                .fileName("file name")
                .url("ya.ru/image.png")
                .build();
    }

    @Operation(
            summary = "Get post attachments",
            description = "User get all post attachments",
            tags = {"attachments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/posts/{postId}/attachments", produces = "application/json")
    public List<AttachmentResponse> getAllPostAttachments(
            @Parameter(description = "Post id")
            @PathVariable UUID postId
    ) {
        return List.of(
                AttachmentResponse.builder()
                        .resourceId(postId)
                        .resourceType(ResourceType.POST)
                        .userId(UUID.randomUUID())
                        .attachmentId(UUID.randomUUID())
                        .attachmentType(AttachmentType.IMAGE)
                        .fileName("file name")
                        .url("ya.ru/image.png")
                        .createdAt(Instant.now())
                        .build()
        );
    }

    @Operation(
            summary = "Delete attachment",
            description = "User delete attachment by id",
            tags = {"attachments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/attachments/{attachmentId}")
    public void deleteAttachmentById(
            @Parameter(description = "Attachment file id")
            @PathVariable UUID attachmentId
    ) {
        // delete attachment
    }


    // ============= reactions ============= //


    @Operation(
            summary = "New post reaction",
            description = "User make a new post reaction",
            tags = {"reactions"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/posts/{postId}/reactions", produces = "application/json", consumes = "application/json")
    public PostReactionShortResponse addPostReaction(
            @Parameter(description = "Post id")
            @PathVariable UUID postId,
            @RequestBody PostReactionRequest postReactionRequest
    ) {
        return PostReactionShortResponse.builder()
                .postId(postId)
                .reactionId(UUID.randomUUID())
                .build();
    }

    @Operation(
            summary = "Get post reactions with pagination",
            description = "User get all post reactions with pagination",
            tags = {"reactions"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/posts/{postId}/reactions", produces = "application/json")
    public List<PostReactionResponse> getAllPostReactions(
            @Parameter(description = "Post id")
            @PathVariable UUID postId,
            @Parameter(description = "The limit of records")
            @RequestParam(required = false, defaultValue = "100") int limit,
            @Parameter(description = "The offset of records")
            @RequestParam(required = false, defaultValue = "0") int offset
    ) {
        return List.of(
                PostReactionResponse.builder()
                        .reactionId(UUID.randomUUID())
                        .postId(postId)
                        .reactionUserId(UUID.randomUUID())
                        .reactionType(ReactionType.LIKE)
                        .createdAt(Instant.now())
                        .build()
        );
    }

    @Operation(
            summary = "Delete reaction",
            description = "User delete reaction by id",
            tags = {"reactions"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/reactions/{reactionId}")
    public void deleteReactionBydId(
            @Parameter(description = "Reaction id")
            @PathVariable UUID reactionId
    ) {
        // delete reaction
    }


    // ============= comments ============= //


    @Operation(
            summary = "New post comment",
            description = "User make a new post comment",
            tags = {"comments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/posts/{postId}/comments", produces = "application/json", consumes = "application/json")
    public PostCommentShortResponse addPostComment(
            @Parameter(description = "Post id")
            @PathVariable UUID postId,
            @RequestBody PostCommentCreateRequest postCommentRequest

    ) {
        return PostCommentShortResponse.builder()
                .postId(postId)
                .commentId(UUID.randomUUID())
                .build();
    }

    @Operation(
            summary = "Get post comments with pagination",
            description = "User get all post comments with pagination",
            tags = {"comments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/posts/{postId}/comments", produces = "application/json")
    public List<PostCommentResponse> getAllPostComments(
            @Parameter(description = "Post id")
            @PathVariable UUID postId,
            @RequestParam(required = false, defaultValue = "100") int limit,
            @Parameter(description = "The offset of records")
            @RequestParam(required = false, defaultValue = "0") int offset
    ) {
        return List.of(
                PostCommentResponse.builder()
                        .commentId(UUID.randomUUID())
                        .postId(postId)
                        .commentUserId(UUID.randomUUID())
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now().plus(3, ChronoUnit.MINUTES))
                        .build()
        );
    }

    @Operation(
            summary = "Update comment",
            description = "User update comment by id",
            tags = {"comments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/comments/{commentId}", produces = "application/json", consumes = "application/json")
    public PostCommentShortResponse updateCommentById(
            @Parameter(description = "Comment id")
            @PathVariable UUID commentId,
            @RequestBody PostCommentUpdateRequest postRequest
    ) {
        return PostCommentShortResponse.builder()
                .commentId(commentId)
                .postId(UUID.randomUUID())
                .build();
    }

    @Operation(
            summary = "Delete comment",
            description = "User delete comment by id",
            tags = {"comments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/comments/{commentId}")
    public void deleteCommentById(
            @Parameter(description = "Comment id")
            @PathVariable UUID commentId
    ) {
        // delete comment
    }


    // ============= users ============= //


    @Operation(
            summary = "Get user info",
            description = "Get user by id",
            tags = {"users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/users/{userId}", produces = "application/json")
    public UserResponse getUserById(
            @Parameter(description = "User id")
            @PathVariable UUID userId
    ) {
        return UserResponse.builder()
                .userId(userId)
                .fullName("Иван Иванович Иванов")
                .description("text about themself")
                .iconUrl("ya.ru/profile-12.jpg")
                .email("email@mail.ru")
                .createdAt(Instant.now())
                .build();
    }

    @Operation(
            summary = "Get users by ids",
            description = "Get user profiles by ids",
            tags = {"users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/users", produces = "application/json")
    public List<UserResponse> getUsersByIds(
            @Parameter(description = "User ids")
            @RequestParam List<UUID> userIds
    ) {
        return List.of(
                UserResponse.builder()
                        .userId(UUID.randomUUID())
                        .fullName("Иван Иванович Иванов")
                        .description("text about themself")
                        .iconUrl("https://image.ru")
                        .email("email@mail.ru")
                        .createdAt(Instant.now())
                        .build()
        );
    }


    // ============= subscriptions ============= //


    @Operation(
            summary = "Get user subscriptions with pagination",
            description = "Get all user subscriptions by id with pagination",
            tags = {"subscriptions"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/users/{userId}/subscriptions", produces = "application/json")
    public List<UserSubscriptionsResponse> getUserSubscriptions(
            @Parameter(description = "User id")
            @PathVariable UUID userId,
            @Parameter(description = "The limit of records")
            @RequestParam(required = false, defaultValue = "10") int limit,
            @Parameter(description = "The offset of records")
            @RequestParam(required = false, defaultValue = "0") int offset
    ) {
        return List.of(
                UserSubscriptionsResponse.builder()
                        .userId(userId)
                        .followingUserId(UUID.randomUUID())
                        .build()
        );
    }

    @Operation(
            summary = "Subscribe to a user",
            description = "User subscribes to another user",
            tags = {"subscriptions"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/users/{userId}/subscriptions", produces = "application/json")
    public UserSubscriptionsResponse addUserSubscription(
            @Parameter(description = "User id")
            @PathVariable UUID userId,
            @Parameter(description = "User id being followed")
            @RequestParam UUID followingUserId
    ) {
        return UserSubscriptionsResponse.builder()
                .userId(userId)
                .followingUserId(followingUserId)
                .build();
    }

    @Operation(
            summary = "Delete user subscription",
            description = "Delete user subscription by id",
            tags = {"subscriptions"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/subscriptions/{subscriptionId}/")
    public void deleteUserSubscriptionById(
            @Parameter(description = "Subscription id between users")
            @PathVariable UUID subscriptionId

    ) {
        // delete comment
    }
}
