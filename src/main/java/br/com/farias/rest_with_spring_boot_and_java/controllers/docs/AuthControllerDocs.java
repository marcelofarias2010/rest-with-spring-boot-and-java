package br.com.farias.rest_with_spring_boot_and_java.controllers.docs;

import br.com.farias.rest_with_spring_boot_and_java.config.WebConstants;
import br.com.farias.rest_with_spring_boot_and_java.data.dto.security.AccountCredentialsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AuthControllerDocs {

    @Operation(
            summary = "Authenticates an user and returns a token",
            description = "Authenticates an user and returns a token",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success",responseCode = "200",content = @Content(schema = @Schema(implementation = AuthControllerDocs.class)))
            })
    ResponseEntity<?> signin(AccountCredentialsDTO credentials);

    @Operation(
            summary = "Refresh token for Authenticates user and returns a token",
            description = "Refresh token for Authenticates user and returns a token",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success",responseCode = "200",content = @Content(schema = @Schema(implementation = AuthControllerDocs.class)))
            })
    ResponseEntity<?> refreshToken(String username, String refreshToken);

    @Operation(
            summary = "Creating a user in the database with an encrypted password",
            description = "Creating a user in the database with an encrypted password",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success",responseCode = "200",content = @Content(schema = @Schema(implementation = AuthControllerDocs.class)))
            })
    AccountCredentialsDTO create(AccountCredentialsDTO credentials);
}
