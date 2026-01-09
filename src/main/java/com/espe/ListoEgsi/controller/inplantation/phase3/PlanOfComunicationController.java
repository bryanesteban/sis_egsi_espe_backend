package com.espe.ListoEgsi.controller.inplantation.phase3;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espe.ListoEgsi.domain.dto.Implantation.phase3.PlanOfComunicationDTO;
import com.espe.ListoEgsi.service.Inplantation.phase3.PlanOfComunicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Plan Of Comunication Controller", description = "Endpoints for managing Plan Of Comunication")
@RequestMapping("/planOfComunication")
@RestController
public class PlanOfComunicationController {

    @Autowired
    private PlanOfComunicationService planOfComunicationService;


    /**
     * Get all Plan Of Comunication entries.
     * @return A list of Plan Of Comunication DTOs.
     */

    @Operation(summary = "Get All Plan Of Comunication",
              description = "Retrieve all Plan Of Comunication entries")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PlanOfComunicationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getAllPlanOfComunication() {
        log.info("Received request to get all Plan Of Comunication entries");
        try {
            List<PlanOfComunicationDTO> planOfComunicationList = planOfComunicationService.findAllPlansOfComunication();
         
            Map<String, Object> response = new HashMap<>();
            response.put("planOfComunication", planOfComunicationList);
            response.put("total", planOfComunicationList.size());
            log.info("Successfully retrieved {} Plan Of Comunication records", planOfComunicationList.size());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error retrieving Plan Of Comunication: " + e.getMessage());
            log.error("Error retrieving Plan Of Comunication entries: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get Plan Of Comunication by ID",
              description = "Retrieve a Plan Of Comunication entry by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Plan Of Comunication",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PlanOfComunicationDTO.class))),
        @ApiResponse(responseCode = "404", description = "Plan Of Comunication not found",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idPlanOfComunication}")
    public ResponseEntity<?> getPlanOfComunicationById(
        @Parameter(description = "ID of the Plan Of Comunication to retrieve", required = true)
        @PathVariable UUID idPlanOfComunication) {
        try {
            PlanOfComunicationDTO planOfComunication = planOfComunicationService.getPlanOfComunication(idPlanOfComunication);
            log.info("Successfully retrieved Plan Of Comunication with ID: {}", idPlanOfComunication);
            return ResponseEntity.status(HttpStatus.OK).body(planOfComunication);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error retrieving Plan Of Comunication: " + e.getMessage());
            log.error("Error retrieving Plan Of Comunication with ID {}: {}", idPlanOfComunication, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Save Plan Of Comunication by Process ID",
                description = "Save a new Plan Of Comunication entry associated with a specific Process ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully saved Plan Of Comunication",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PlanOfComunicationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> savePlanOfComunication(@Valid @RequestBody PlanOfComunicationDTO planOfComunicationSubmit,
        BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
                log.error("Validation errors while saving Plan Of Comunication: {}", errors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            try {
                PlanOfComunicationDTO savedPlanOfComunication = planOfComunicationService.savePlanOfComunication(planOfComunicationSubmit);
                log.info("Successfully saved Plan Of Comunication with ID: {}", savedPlanOfComunication.getIdPlanComunication());
                return ResponseEntity.status(HttpStatus.OK).body(savedPlanOfComunication);
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Error saving Plan Of Comunication: " + e.getMessage());
                log.error("Error saving Plan Of Comunication: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
    }

    @Operation(summary = "Update Plan Of Comunication",
                description = "Update an existing Plan Of Comunication entry")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Plan Of Comunication",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PlanOfComunicationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Plan Of Comunication not found",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updatePlanOfComunication(
        @Valid @RequestBody PlanOfComunicationDTO planOfComunicationUpdate,
        BindingResult bindingResult) {

            if(bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
                log.error("Validation errors while updating Plan Of Comunication: {}", errors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            try {
                PlanOfComunicationDTO updatedPlanOfComunication = planOfComunicationService.updatePlanOfComunication(planOfComunicationUpdate);
                log.info("Successfully updated Plan Of Comunication with ID: {}", updatedPlanOfComunication.getIdPlanComunication());
                return ResponseEntity.status(HttpStatus.OK).body(updatedPlanOfComunication);
            } catch (Exception e) {
                log.error("Error updating Plan Of Comunication: {}", e.getMessage());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Error updating Plan Of Comunication: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
    }

    @Operation(summary = "Delete Plan Of Comunication by ID",
              description = "Delete a Plan Of Comunication entry by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted Plan Of Comunication",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Plan Of Comunication not found",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("delete/{idPlanOfComunication}")
    public ResponseEntity<?> deletePlanOfComunication(
        @Parameter(description = "ID of the Plan Of Comunication to delete", required = true)
        @PathVariable UUID idPlanOfComunication) {
            try {
                planOfComunicationService.deletePlanOfComunication(idPlanOfComunication);
                log.info("Successfully deleted Plan Of Comunication with ID: {}", idPlanOfComunication);
                Map<String, String> successResponse = new HashMap<>();
                successResponse.put("message", "Plan Of Comunication deleted successfully");
                return ResponseEntity.noContent().build();
            } catch (Exception e) {
                log.error("Error deleting Plan Of Comunication with ID {}: {}", idPlanOfComunication, e.getMessage());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Error deleting Plan Of Comunication: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
    }

}





