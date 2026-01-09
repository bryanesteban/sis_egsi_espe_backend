package com.espe.ListoEgsi.controller.inplantation.phase2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espe.ListoEgsi.domain.dto.Implantation.phase2.ScopeEgsiDTO;
import com.espe.ListoEgsi.service.Inplantation.phase2.ScopeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Tag(name = "ScopeEgsiController", description = "Controller for managing Scope Egsi operations")
@RequestMapping("/scopeEgsi")
@RestController
public class ScopeEgsiController {

    @Autowired
    private ScopeService scopeService;

    /**
     * Get all Scope Egsi records.
     * @return List of ScopeEgsiDTO
     */

    @Operation(summary = "Get all Scope Egsi records", 
               description = "Retrieve a list of all Scope Egsi records")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ScopeEgsiDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
        
    })
    @GetMapping
    public ResponseEntity<?> getAllScopes() {
        log.info("Received request to get all Scope Egsi records");
        try {
            List<ScopeEgsiDTO> scopes = scopeService.findAllScopes();

            Map<String, Object> response = new HashMap<>();
            response.put("scopes", scopes);
            response.put("total", scopes.size());
            log.info("Successfully retrieved {} Scope Egsi records", scopes.size());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error retrieving Scope Egsi"+ e.getMessage());
            log.error("Error occurred while retrieving Scope Egsi records: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Save a new Scope Egsi record", 
               description = "Create and save a new Scope Egsi record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created Scope Egsi record",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ScopeEgsiController.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> saveScope(@Valid @RequestBody ScopeEgsiDTO scopeSave,
        BindingResult bindingResult) {
        
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage()));
            log.warn("Validation errors while saving Scope Egsi: {}", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        try {
            ScopeEgsiDTO savedScope = scopeService.saveScope(scopeSave);
            log.info("Successfully saved Scope Egsi with ID: {}", savedScope.getIdScope());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedScope);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error saving Scope Egsi: " + e.getMessage());
            log.error("Error occurred while saving Scope Egsi: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "find Scope Egsi by idScope", 
               description = "Retrieve a Scope Egsi record by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Scope Egsi record",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ScopeEgsiController.class))),
        @ApiResponse(responseCode = "404", description = "Scope Egsi not found",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idScope}")
    public ResponseEntity<?> getScopeEgsiById(
        @Parameter(description = "ID of the Scope Egsi to be retrieved", required = true)
        @PathVariable UUID idScope){
        try {
            ScopeEgsiDTO scopeFound = scopeService.getScope(idScope);
            log.info("Successfully retrieved Scope Egsi with ID: {}", idScope);
            return ResponseEntity.status(HttpStatus.OK).body(scopeFound);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error retrieving Scope Egsi: " + e.getMessage());
            log.error("Error occurred while retrieving Scope Egsi with ID {}: {}", idScope, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Update an existing Scope Egsi record", 
               description = "Update the details of an existing Scope Egsi record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Scope Egsi record",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ScopeEgsiController.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Scope Egsi not found",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updateScope(
        @Valid @RequestBody ScopeEgsiDTO scopeUpdate,
        BindingResult bindingResult) {
        
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage()));
            log.warn("Validation errors while updating Scope Egsi: {}", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        try {
            ScopeEgsiDTO updatedScope = scopeService.updateScope(scopeUpdate);
            log.info("Successfully updated Scope Egsi with ID: {}", updatedScope.getIdScope());
            return ResponseEntity.status(HttpStatus.OK).body(updatedScope);
        } catch (Exception e) {
            log.error("Error occurred while updating Scope Egsi: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error updating Scope Egsi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @Operation(summary = "Delete a Scope Egsi record", 
               description = "Delete a Scope Egsi record by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted Scope Egsi record",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ScopeEgsiController.class))),
        @ApiResponse(responseCode = "404", description = "Scope Egsi not found",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{idScope}")
    public ResponseEntity<?> deleteScope(
        @Parameter(description = "ID of the Scope Egsi to be deleted", required = true)
        @PathVariable UUID idScope) {
            log.info("Request to delete Scope Egsi with ID: {}", idScope);
        try {
            scopeService.deleteScope(idScope);
            log.info("Successfully deleted Scope Egsi with ID: {}", idScope);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error occurred while deleting Scope Egsi with ID {}: {}", idScope, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error deleting Scope Egsi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
