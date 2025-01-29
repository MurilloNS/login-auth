package com.murillons.login_auth.documentation;

import com.murillons.login_auth.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "User Api")
public interface UserApi {
    @Operation(summary = "Register a user", description = "Register a user entity and its data")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "created user"), @ApiResponse(responseCode = "400", description = "requisition failed")})
    ResponseEntity<?> registerUser(@Valid @RequestBody User user);
}