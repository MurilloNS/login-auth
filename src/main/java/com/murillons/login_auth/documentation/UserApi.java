package com.murillons.login_auth.documentation;

import com.murillons.login_auth.dto.UserRequest;
import com.murillons.login_auth.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "User API")
public interface UserApi {
    @Operation(summary = "Register a user", description = "Register a user entity and its data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = "{\"email\": \"user@example.com\", \"role\": \"USER\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict - Email already registered",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-02-06T10:38:28.117154300Z\", \"status\": 409, \"error\": \"Conflict\", \"message\": \"Este e-mail já está cadastrado!\", \"path\": \"/api/user/register\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-02-06T10:38:28.117154300Z\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Invalid request data\", \"path\": \"/api/user/register\"}")
                    )
            )
    })
    @PostMapping("/register")
    ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest);

    @Operation(summary = "User login", description = "Authenticate a user and return a JWT token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-02-07T10:38:28.117154300Z\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuário não encontrado.\", \"path\": \"/api/user/login\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-02-07T10:38:28.117154300Z\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Senha inválida.\", \"path\": \"/api/user/login\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid input",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-02-07T10:38:28.117154300Z\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Invalid request data\", \"path\": \"/api/user/login\"}")
                    )
            )
    })
    @PostMapping("/login")
    ResponseEntity<?> login(@Valid @RequestBody UserRequest request);
}