package com.espe.ListoEgsi.controller.inplantation.phase4;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.espe.ListoEgsi.domain.dto.Implantation.phase4.PlanInternalEvaluationDTO;
import com.espe.ListoEgsi.service.Inplantation.phase4.PlanInternalEvaluationService;

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
@Tag(name = "PlanInternalEvaluation Controller", description = "Controller for managing Plan of Internal Evaluation operations")
@RequestMapping("/planInternalEvaluation")
@RestController
public class PlanInternalEvaluationController {

    @Autowired
    private PlanInternalEvaluationService planInternalEvaluationService;

    /**
     * Get all Plans of Internal Evaluation
     * @return A list of PlanInternalEvaluationDTO
     */

    @Operation(summary = "Get all Plans of Internal Evaluation", 
               description = "Retrieve a list of all Plans of Internal Evaluation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all Plans of Internal Evaluation",
                        content = @Content(mediaType = "application/json",
                        schema =  @Schema(implementation = PlanInternalEvaluationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request",
                        content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                        content = @Content(mediaType = "application/json"))
        
    })
    @GetMapping
    public ResponseEntity<?> getAllPlansInternalEvaluation() {
        try {
            List<PlanInternalEvaluationDTO> plansInternals = planInternalEvaluationService.findAllPlansInternalEvaluation();
            
            Map<String, Object> response = new HashMap<>();
            response.put("plansInternalEvaluations", plansInternals);
            response.put("totalPlansInternalEvaluations", plansInternals.size());
            log.info("Successfully retrieved all Plans of Internal Evaluation. Total: {}", plansInternals.size());
            
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while retrieving Plans of Internal Evaluation: " + e.getMessage());
            log.error("Error retrieving Plans of Internal Evaluation: {}", e.getMessage()); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get Plan of Internal Evaluation by ID", 
               description = "Retrieve a Plan of Internal Evaluation by its unique ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the Plan of Internal Evaluation",
                        content = @Content(mediaType = "application/json",
                        schema =  @Schema(implementation = PlanInternalEvaluationDTO.class))),
        @ApiResponse(responseCode = "404", description = "Plan of Internal Evaluation not found",
                        content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                        content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idPlanInternalEval}")
    public ResponseEntity<?> getPlanInternalEvaluationById(
        @Parameter(description = "ID of the Plan of Internal Evaluation to retrieve", required = true)
        @PathVariable UUID idPlanInternalEval) {
        try {
            PlanInternalEvaluationDTO planInternalEvaluation = planInternalEvaluationService.getPlanInternalEvaluation(idPlanInternalEval);
            log.info("Successfully retrieved Plan of Internal Evaluation with ID: {}", idPlanInternalEval);
            return ResponseEntity.status(HttpStatus.OK).body(planInternalEvaluation);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while retrieving the Plan of Internal Evaluation: " + e.getMessage());
            log.error("Error retrieving Plan of Internal Evaluation with ID {}: {}", idPlanInternalEval, e.getMessage()); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Create a new Plan of Internal Evaluation", 
               description = "Create and save a new Plan of Internal Evaluation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Plan of Internal Evaluation created successfully",
                        content = @Content(mediaType = "application/json",
                        schema =  @Schema(implementation = PlanInternalEvaluationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request",
                        content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                        content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createPlanInternalEvaluation(@Valid @RequestBody PlanInternalEvaluationDTO planInternalEvaluationDTO,
        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage()));
                log.error("Validation errors while creating Plan of Internal Evaluation: {}", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

            try {
                PlanInternalEvaluationDTO createdPlanInternalEvaluation =
                    planInternalEvaluationService.savePlanInternalEvaluation(planInternalEvaluationDTO);
                log.info("Successfully created Plan of Internal Evaluation with ID: {}", createdPlanInternalEvaluation.getIdPlanInternalEval());
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPlanInternalEvaluation);

            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An error occurred while creating the Plan of Internal Evaluation: " + e.getMessage());
                log.error("Error creating Plan of Internal Evaluation: {}", e.getMessage()); 
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
     }

    @Operation(summary = "Update an existing Plan of Internal Evaluation", 
               description = "Update the details of an existing Plan of Internal Evaluation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plan of Internal Evaluation updated successfully",
                        content = @Content(mediaType = "application/json",
                        schema =  @Schema(implementation = PlanInternalEvaluationDTO.class))),  
                        @ApiResponse( responseCode = "400", description = "Bad Request",
                        content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Plan of Internal Evaluation not found",
                        content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                        content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updatePlanInternalEvaluation(
        @Valid @RequestBody PlanInternalEvaluationDTO planInternalEvaluationDTO,
        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage()));
                log.error("Validation errors while updating Plan of Internal Evaluation: {}", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

            try {
                PlanInternalEvaluationDTO updatedPlanInternalEvaluation =
                    planInternalEvaluationService.updatePlanInternalEvaluation(planInternalEvaluationDTO);
                log.info("Successfully updated Plan of Internal Evaluation with ID: {}", updatedPlanInternalEvaluation.getIdPlanInternalEval());
                return ResponseEntity.status(HttpStatus.OK).body(updatedPlanInternalEvaluation);

            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An error occurred while updating the Plan of Internal Evaluation: " + e.getMessage());
                log.error("Error updating Plan of Internal Evaluation: {}", e.getMessage()); 
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
     }

     @Operation(summary = "Delete a Plan of Internal Evaluation", 
                description = "Delete a Plan of Internal Evaluation by its unique ID")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan of Internal Evaluation deleted successfully",
                            content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Plan of Internal Evaluation not found",
                            content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json"))
        })
    @DeleteMapping("delete/{idPlanInternalEval}")
    public ResponseEntity<?> deletePlanInternalEvaluation(
            @Parameter(description = "ID of the Plan of Internal Evaluation to delete", required = true)
            @PathVariable UUID idPlanInternalEval) {
            try {
                planInternalEvaluationService.deletePlanInternalEvaluation(idPlanInternalEval);
                log.info("Successfully deleted Plan of Internal Evaluation with ID: {}", idPlanInternalEval);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Plan of Internal Evaluation deleted successfully.");
                return ResponseEntity.status(HttpStatus.OK).body(response);

            } catch (Exception e) {
                log.error("Error deleting Plan of Internal Evaluation with ID {}: {}", idPlanInternalEval, e.getMessage()); 
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An error occurred while deleting the Plan of Internal Evaluation: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }

    }