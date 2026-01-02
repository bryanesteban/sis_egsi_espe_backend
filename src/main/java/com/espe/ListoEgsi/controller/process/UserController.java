package com.espe.ListoEgsi.controller.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espe.ListoEgsi.domain.dto.setting.UserDTO;
import com.espe.ListoEgsi.domain.dto.setting.UserModDTO;
import com.espe.ListoEgsi.service.process.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "User", description = "API for managing user")
@RequestMapping("/users")
@RestController
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * Get all available users
     * @return List of all active users
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserDTO> users = userService.findAllUsers();
            
            Map<String, Object> response = new HashMap<>();
            response.put("users", users);
            response.put("total", users.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error retrieving users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Save user",
               description = "Save a new user ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User saved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<Object> saveNewUser(
        @Valid @RequestBody UserDTO userSave, 
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in submit user answer request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            
            UserDTO userAnswer = userService.saveUser(userSave);
            log.info("Successfully submitted user with ID: {}", userSave.getCedula());
            return ResponseEntity.status(HttpStatus.CREATED).body(userAnswer);
        } catch (Exception e) {
            log.error("Error submitting user with ID: {}", userSave.getCedula(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @Operation(summary = "find user",
               description = "find user by id ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User found successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idUser}")
    public ResponseEntity<?> getUserById( 
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID idUser) {    
        try {

            UserDTO userFound = userService.getUser(idUser);
            return ResponseEntity.status(HttpStatus.OK).body(userFound);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error retrieving users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Update user ",
               description = "Updates an existing user ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User  updated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = UserModDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "User not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<Object> modifyUser(
        @Valid @RequestBody UserModDTO userMod,
        BindingResult bindingResult){
            if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in submit user answer request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
            }

            try {
            UserModDTO userAnswer = userService.updateUser(userMod);
            log.info("Successfully updated user with ID: {}", userMod.getCedula());
            return ResponseEntity.status(HttpStatus.CREATED).body(userAnswer);
            } catch (Exception e) {
                log.error("Error updated user with ID: {}", userMod.getCedula(), e);
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }


    }

    @Operation(summary = "Delete user",
               description = "Soft deletes a user by setting is_deleted to true")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User  not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{idUser}")
    public ResponseEntity<Object> deleteUserAnswer(
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID idUser) {

        log.info("Request to delete User with ID: {}", idUser);

        try {
            userService.softDeleteUser(idUser);
            log.info("Successfully deleted User with ID: {}", idUser);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting User with ID: {}", idUser, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
}
