package com.espe.ListoEgsi.controller.inplantation.phase9;

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

import com.espe.ListoEgsi.domain.dto.Implantation.phase9.RiskTreatmentPlanDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase9.RiskTreatmentPlan;
import com.espe.ListoEgsi.service.Inplantation.phase9.RiskTreatmentPlanService;

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
@Tag(name = "Risk Treatment Plan", description = "Controller for managing Risk Treatment Plan operations")
@RequestMapping("/riskTreatmentPlan")
@RestController
public class RiskTreatmentPlanController {

     @Autowired
    private RiskTreatmentPlanService riskTreatmentPlanService;

    /**
     * Gets the Risk Treatment Plan Service.
     * @param riskTreatmentPlanService the service to set
     * @return the Risk Treatment Plan Service
     */

    @Operation(summary = "Get Risk Treatment Plan by ID", 
               description = "Retrieves a Risk Treatment Plan by its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of Risk Treatment Plan",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RiskTreatmentPlan.class))),
        @ApiResponse(responseCode = "404", description = "Risk Treatment Plan not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))     
    })
    @GetMapping
    public ResponseEntity<?> getRiskTreatmentPlans() {
        log.info("Received request to get Risk Treatment Plans");
        List<RiskTreatmentPlanDTO> riskTreatmentPlans = riskTreatmentPlanService.findAllRiskTreatmentPlans();

        Map<String, Object> response = new HashMap<>();
        response.put("riskTreatmentPlans", riskTreatmentPlans);
        response.put("totalItems", riskTreatmentPlans.size());
        log.info("Successfully retrieved {} Risk Treatment Plans", riskTreatmentPlans.size());
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Get Risk Treatment Plan by ID", 
               description = "Retrieves a Risk Treatment Plan by its unique identifier.")
               @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Risk Treatment Plan",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RiskTreatmentPlanDTO.class))),   
        @ApiResponse(responseCode = "404", description = "Risk Treatment Plan not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idRiskTreatmentPlan}")
    public ResponseEntity<?> getRiskTreatmentPlanById(
        @Parameter(description = "ID of the Risk Treatment Plan to be fetched", required = true)
        @PathVariable UUID idRiskTreatmentPlan){
        try {
            log.info("Received request to fetch Risk Treatment Plan with ID: {}", idRiskTreatmentPlan);
            RiskTreatmentPlanDTO riskTreatmentPlan = riskTreatmentPlanService.getRiskTreatmentPlan(idRiskTreatmentPlan);
            log.info("Successfully retrieved Risk Treatment Plan with ID: {}", idRiskTreatmentPlan);
            return ResponseEntity.status(HttpStatus.OK).body(riskTreatmentPlan);
        }  catch (Exception e) {
            log.error("Internal server error while fetching Risk Treatment Plan with ID: {}: {}", idRiskTreatmentPlan, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Risk Treatment Plan not found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Create Risk Treatment Plan", 
               description = "Creates a new Risk Treatment Plan associated with a specific Process ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Risk Treatment Plan",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RiskTreatmentPlan.class))),
        @ApiResponse(responseCode = "400", description = "Bad request, invalid ",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))     
    })
    @PostMapping
    public ResponseEntity<?> createRiskTreatmentPlan(@Valid @RequestBody RiskTreatmentPlanDTO riskTreatmentPlanSubmit,
        BindingResult bindingResult) {
            System.out.println("riskTreatmentPlanSubmit:"+riskTreatmentPlanSubmit);
            if(bindingResult.hasErrors()){
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                    log.error("Validation error on field {}: {}", error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }
            try{
                RiskTreatmentPlanDTO riskTreatmentPlanSaved = riskTreatmentPlanService.saveRiskTreatmentPlan(riskTreatmentPlanSubmit);
                log.info("Successfully saved Risk Treatment Plan with ID: {}", riskTreatmentPlanSaved.getIdPlanTratamient());
                return ResponseEntity.status(HttpStatus.CREATED).body(riskTreatmentPlanSaved);
            } catch (Exception e){
                log.error("Error saving Risk Treatment Plan: {}", e.getMessage());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An error occurred while saving Risk Treatment Plan.:"+e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Update Risk Treatment Plan",
               description = "Update an existing Risk Treatment Plan in Phase 9 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully updated Risk Treatment Plan",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = RiskTreatmentPlan.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Risk Treatment Plan not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updateRiskTreatmentPlan(@Valid @RequestBody RiskTreatmentPlanDTO riskTreatmentPlanUpdate,
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
            RiskTreatmentPlanDTO riskTreatmentPlanUpdated = riskTreatmentPlanService.updateRiskTreatmentPlan(riskTreatmentPlanUpdate);
            log.info("Successfully updated Risk Treatment Plan with ID: {}", riskTreatmentPlanUpdated.getIdPlanTratamient());
            return ResponseEntity.status(HttpStatus.OK).body(riskTreatmentPlanUpdated);
        } catch (Exception e){
            log.error("Error updating Risk Treatment Plan with ID {}: {}", riskTreatmentPlanUpdate.getIdPlanTratamient(), e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while updating Risk Treatment Plan with ID: " + riskTreatmentPlanUpdate.getIdPlanTratamient());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @Operation(summary = "Delete Risk Treatment Plan",
               description = "Delete an existing Risk Treatment Plan in Phase 9 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully deleted Risk Treatment Plan",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = RiskTreatmentPlan.class))),
        @ApiResponse(responseCode = "404", description = "Risk Treatment Plan not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{idRiskTreatmentPlan}")
    public ResponseEntity<?> deleteSecurityPolicies(
        @Parameter(description = "ID of the Risk Treatment Plan to be deleted", required = true)
        @PathVariable UUID idRiskTreatmentPlan){
        try{
            riskTreatmentPlanService.deleteRiskTreatmentPlan(idRiskTreatmentPlan);
            log.info("Successfully deleted Risk Treatment Plan with ID: {}", idRiskTreatmentPlan);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Risk Treatment Plan with ID: " + idRiskTreatmentPlan + " has been deleted.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Error deleting Risk Treatment Plan with ID {}: {}", idRiskTreatmentPlan, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while deleting Risk Treatment Plan with ID: " + idRiskTreatmentPlan);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
