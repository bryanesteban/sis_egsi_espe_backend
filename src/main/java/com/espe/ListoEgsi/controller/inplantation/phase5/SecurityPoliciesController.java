package com.espe.ListoEgsi.controller.inplantation.phase5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espe.ListoEgsi.domain.dto.Implantation.phase5.SecurityPoliciesDTO;
import com.espe.ListoEgsi.service.Inplantation.phase5.SecurityPoliciesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Tag(name = "Security Policies Controller", description = "Endpoints for managing Security Policies in Phase 5 of Inplantation" )
@RequestMapping("/securityPolicies")
@RestController
public class SecurityPoliciesController {

    @Autowired
    private SecurityPoliciesService securityPoliciesService;

    /**
     * Get all Security Policies
     * @return List of SecurityPoliciesDTO
     */

    @Operation(summary = "Get all Security Policies",
               description = "Retrieve a list of all Security Policies in Phase 5 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Security Policies",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SecurityPoliciesController.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getAllPlanOfComunications(){
        try{
            List<SecurityPoliciesDTO> securityPoliciesList = securityPoliciesService.findAllSecurityPolicies();

            Map<String, Object> response =new HashMap<>();
            response.put("securityPolicies", securityPoliciesList);
            response.put("totalSecurityPolicies", securityPoliciesList.size());
            log.info("Successfully retrieved {} Security Policies", securityPoliciesList.size());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Error retrieving Security Policies: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while fetching Security Policies.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "Get Security Policies by ID",
               description = "Retrieve a specific Security Policies by its ID in Phase 5 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Security Policies",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SecurityPoliciesController.class))),
        @ApiResponse(responseCode = "404", description = "Security Policies not found",
                content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/find/{idSecurityPolicies}")
    public ResponseEntity<?> getSecurityPoliciesById(
        @Parameter(description = "ID of the Security Policies to be retrieved", required = true)
        @PathVariable UUID idSecurityPolicies){
        try{
            SecurityPoliciesDTO securityPolicies = securityPoliciesService.getSecurityPolicies(idSecurityPolicies);
            log.info("Successfully retrieved Security Policies with ID: {}", idSecurityPolicies);
            return ResponseEntity.status(HttpStatus.OK).body(securityPolicies);
        } catch (Exception e){
            log.error("Error retrieving Security Policies with ID {}: {}", idSecurityPolicies, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while fetching Security Policies with ID: " + idSecurityPolicies);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Save Security Policies",
               description = "Save a new Security Policies in Phase 5 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "201", description = "Successfully saved Security Policies",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SecurityPoliciesController.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> saveSecurityPolicies(@Valid @RequestBody SecurityPoliciesDTO securityPoliciesSubmit,
        BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errorResponse = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errorResponse.put(error.getField(), error.getDefaultMessage());
                log.error("Validation error on field '{}': {}", error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try{
            SecurityPoliciesDTO securityPoliciesSaved = securityPoliciesService.saveSecurityPolicies(securityPoliciesSubmit);
            log.info("Successfully saved Security Policies with ID: {}", securityPoliciesSaved.getIdSecurityPolicies());
            return ResponseEntity.status(HttpStatus.CREATED).body(securityPoliciesSaved);
        } catch (Exception e){
            log.error("Error saving Security Policies: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while saving Security Policies.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Update Security Policies",
               description = "Update an existing Security Policies in Phase 5 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully updated Security Policies",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SecurityPoliciesController.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Security Policies not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updateSecurityPolicies(@Valid @RequestBody SecurityPoliciesDTO securityPoliciesUpdate,
        BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errorResponse = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errorResponse.put(error.getField(), error.getDefaultMessage());
                log.error("Validation error on field '{}': {}", error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try{
            SecurityPoliciesDTO securityPoliciesUpdated = securityPoliciesService.updateSecurityPolicies(securityPoliciesUpdate);
            log.info("Successfully updated Security Policies with ID: {}", securityPoliciesUpdated.getIdSecurityPolicies());
            return ResponseEntity.status(HttpStatus.OK).body(securityPoliciesUpdated);
        } catch (Exception e){
            log.error("Error updating Security Policies with ID {}: {}", securityPoliciesUpdate.getIdSecurityPolicies(), e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while updating Security Policies with ID: " + securityPoliciesUpdate.getIdSecurityPolicies());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Delete Security Policies",
               description = "Delete an existing Security Policies in Phase 5 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully deleted Security Policies",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SecurityPoliciesController.class))),
        @ApiResponse(responseCode = "404", description = "Security Policies not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{idSecurityPolicies}")
    public ResponseEntity<?> deleteSecurityPolicies(
        @Parameter(description = "ID osf the Security Policies to be deleted", required = true)
        @PathVariable UUID idSecurityPolicies){
        try{
            securityPoliciesService.deleteSecurityPolicies(idSecurityPolicies);
            log.info("Successfully deleted Security Policies with ID: {}", idSecurityPolicies);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Security Policies with ID: " + idSecurityPolicies + " has been deleted.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Error deleting Security Policies with ID {}: {}", idSecurityPolicies, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while deleting Security Policies with ID: " + idSecurityPolicies);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
