package com.espe.ListoEgsi.controller.inplantation.phase6;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espe.ListoEgsi.domain.dto.Implantation.phase6.MethodologyEvaluationDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase6.MethodologyEvaluation;
import com.espe.ListoEgsi.service.Inplantation.phase6.MethodologyEvaluationService;

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
@Tag(name = "Methodology Evaluation Controller", description = "APIs for managing Methodology Evaluations in Phase 6 of Inplantation")
@RequestMapping("/methodologyEvaluation")
@RestController
public class MethodologyEvaluationController {


    @Autowired
    private MethodologyEvaluationService methodologyEvaluationService;

    /**
     * Gets all Methodology Evaluation .
     * @param idMethodologyEval the ID of the Methodology Evaluation
     * @return the Methodology Evaluation DTO
     */

    @Operation(summary = "Get Methodology Evaluation by ID", 
               description = "Fetches a Methodology Evaluation by its unique ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Methodology Evaluation",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MethodologyEvaluation.class))),
        @ApiResponse(responseCode = "404", description = "Methodology Evaluation not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))     
    })
    @GetMapping
    public ResponseEntity<?> getAllMethodologyEvaluation() {
        log.info("Received request to fetch all Methodology Evaluations");
        List<MethodologyEvaluationDTO> methodologyEvaluations = methodologyEvaluationService.findAllMethodologyEvaluations();

        Map<String, Object> response = new HashMap<>();
        response.put("methodologyEvaluations", methodologyEvaluations);
        response.put("totalItems", methodologyEvaluations.size());
        log.info("Returning {} Methodology Evaluations", methodologyEvaluations.size());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get Methodology Evaluation by ID", 
               description = "Fetches a Methodology Evaluation by its unique ID.")
               @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Methodology Evaluation",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MethodologyEvaluation.class))),   
        @ApiResponse(responseCode = "404", description = "Methodology Evaluation not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idMethodologyEval}")
    public ResponseEntity<?> getMethodologyEvaluationById(
        @Parameter(description = "ID of the Methodology Evaluation to be fetched", required = true)
        @PathVariable UUID idMethodologyEval){
        try {
            log.info("Received request to fetch Methodology Evaluation with ID: {}", idMethodologyEval);
            MethodologyEvaluationDTO methodologyEvaluation = methodologyEvaluationService.getMethodologyEvaluation(idMethodologyEval);
            log.info("Successfully retrieved Methodology Evaluation with ID: {}", idMethodologyEval);
            return ResponseEntity.status(HttpStatus.OK).body(methodologyEvaluation);
        }  catch (Exception e) {
            log.error("Internal server error while fetching Methodology Evaluation with ID: {}: {}", idMethodologyEval, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Methodology Evaluation don't found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get Methodology Evaluations by Process ID", 
               description = "Fetches all Methodology Evaluations associated with a specific Process ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Methodology Evaluations",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MethodologyEvaluation.class))),
        @ApiResponse(responseCode = "400", description = "Bad request, invalid ",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))     
    })
    @PostMapping
    public ResponseEntity<?> CreateMethodologyEvaluation(@Valid @RequestBody MethodologyEvaluationDTO methodologyEvaluationSubmit,
        BindingResult bindingResult) {
            if(bindingResult.hasErrors()){
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                    log.error("Validation error on field {}: {}", error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            try{
                MethodologyEvaluationDTO methodologyEvaluationSaved = methodologyEvaluationService.saveMethodologyEvaluation(methodologyEvaluationSubmit);
                log.info("Successfully saved Methodology of Evaluation with ID: {}",methodologyEvaluationSaved.getIdMethodologyEval() );
                return ResponseEntity.status(HttpStatus.CREATED).body(methodologyEvaluationSaved);
            } catch (Exception e){
                log.error("Error saving Methodology of Evaluation: {}", e.getMessage());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An error occurred while saving Methodology of Evaluation.:"+e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Update Methodology of Evaluation",
               description = "Update an existing Methodology of Evaluation in Phase 6 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully updated Methodology of Evaluation",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MethodologyEvaluation.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Methodology of Evaluation not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updateMethodologyEvaluation(@Valid @RequestBody MethodologyEvaluationDTO methodologyEvaluationUpdate,
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
            MethodologyEvaluationDTO methodologyEvaluationUpdated = methodologyEvaluationService.updateMethodologyEvaluation(methodologyEvaluationUpdate);
            log.info("Successfully updated Methodology of Evaluation with ID: {}", methodologyEvaluationUpdated.getIdMethodologyEval());
            return ResponseEntity.status(HttpStatus.OK).body(methodologyEvaluationUpdated);
        } catch (Exception e){
            log.error("Error updating Methodology of Evaluation with ID {}: {}", methodologyEvaluationUpdate.getIdMethodologyEval(), e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while updating Methodology of Evaluation with ID: " + methodologyEvaluationUpdate.getIdMethodologyEval());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @Operation(summary = "Delete Methodology of Evaluation",
               description = "Delete an existing Methodology of Evaluation in Phase 6 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully deleted Methodology of Evaluation",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MethodologyEvaluation.class))),
        @ApiResponse(responseCode = "404", description = "Methodology of Evaluation not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{idMethodologyEvaluation}")
    public ResponseEntity<?> deleteSecurityPolicies(
        @Parameter(description = "ID osf the Methodology of Evaluation to be deleted", required = true)
        @PathVariable UUID idMethodologyEvaluation){
        try{
            methodologyEvaluationService.deleteMethodologyEvaluation(idMethodologyEvaluation);
            log.info("Successfully deleted Methodology of Evaluation with ID: {}", idMethodologyEvaluation);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Methodology of Evaluation with ID: " + idMethodologyEvaluation + " has been deleted.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Error deleting Methodology of Evaluation with ID {}: {}", idMethodologyEvaluation, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while deleting Methodology of Evaluation with ID: " + idMethodologyEvaluation);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


        
}
