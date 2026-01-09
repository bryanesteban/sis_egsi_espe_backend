package com.espe.ListoEgsi.controller.inplantation.phase7;

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

import com.espe.ListoEgsi.domain.dto.Implantation.phase7.ReportEvaluationOfTreatmentRiskDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase7.ReportEvaluationOfTreatmentRisk;
import com.espe.ListoEgsi.service.Inplantation.phase7.ReportEvaluationOfTreatmentRiskService;

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
@Tag(name = "Report Evaluation Of Treatment Risk Controller", description = "Controller for managing Report Evaluation Of Treatment Risk operations")
@RequestMapping("/reportEvaluationRisk")
@RestController
public class ReportEvaluationOfTreatmentRiskController {


    @Autowired
    private ReportEvaluationOfTreatmentRiskService reportEvaluationRiskService;

    /**
     * Gets the Report Evaluation Of Treatment Risk Service.
     * @param reportEvaluationOfTreatmentRiskService the service to set
     * @return the Report Evaluation Of Treatment Risk Service
     */

    @Operation(summary = "Get Report Evaluation Of Treatment Risk by ID", 
               description = "Retrieves a Report Evaluation Of Treatment Risk by its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of Report Evaluation Of Treatment Risk",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReportEvaluationOfTreatmentRisk.class))),
        @ApiResponse(responseCode = "404", description = "Report Evaluation Of Treatment Risk not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))     
    })
    @GetMapping
    public ResponseEntity<?> getReportEvaluationOfTreatmentRisk() {
        log.info("Received request to get Report Evaluation Of Treatment Risk");
        List<ReportEvaluationOfTreatmentRiskDTO> reportEvaluationRisks = reportEvaluationRiskService.findAllReportEvaluationOfTreatmentRisks();

        Map<String, Object> response = new HashMap<>();
        response.put("reportEvaluationRisks", reportEvaluationRisks);
        response.put("totalItems", reportEvaluationRisks.size());
        log.info("Successfully retrieved {} Report Evaluation Of Treatment Risks", reportEvaluationRisks.size());
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Get Report Evaluation Of Treatment Risk by ID", 
               description = "Retrieves a Report Evaluation Of Treatment Risk by its unique identifier.")
               @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Report Evaluation Of Treatment Risk",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReportEvaluationOfTreatmentRisk.class))),   
        @ApiResponse(responseCode = "404", description = "Report Evaluation Of Treatment Risk not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idReportEvaluationRisk}")
    public ResponseEntity<?> getReportEvaluationOfTreatmentRiskById(
        @Parameter(description = "ID of the Report Evaluation Of Treatment Risk to be fetched", required = true)
        @PathVariable UUID idReportEvaluationRisk){
        try {
            log.info("Received request to fetch Report Evaluation Of Treatment Risk with ID: {}", idReportEvaluationRisk);
            ReportEvaluationOfTreatmentRiskDTO reportEvaluationRisk = reportEvaluationRiskService.getReportEvaluationOfTreatmentRisk(idReportEvaluationRisk);
            log.info("Successfully retrieved Report Evaluation Of Treatment Risk with ID: {}", idReportEvaluationRisk);
            return ResponseEntity.status(HttpStatus.OK).body(reportEvaluationRisk);
        }  catch (Exception e) {
            log.error("Internal server error while fetching Report Evaluation Of Treatment Risk with ID: {}: {}", idReportEvaluationRisk, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Report Evaluation Of Treatment Risk not found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Create report Evaluation Of Treatment Risk", 
               description = "Creates a new Report Evaluation Of Treatment Risk associated with a specific Process ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Report Evaluation Of Treatment Risk",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReportEvaluationOfTreatmentRisk.class))),
        @ApiResponse(responseCode = "400", description = "Bad request, invalid ",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))     
    })
    @PostMapping
    public ResponseEntity<?> CreateReportEvaluationOfTreatmentRisk(@Valid @RequestBody ReportEvaluationOfTreatmentRiskDTO reportEvaluationRiskSubmit,
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
                ReportEvaluationOfTreatmentRiskDTO reportEvaluationRiskSaved = reportEvaluationRiskService.saveReportEvaluationOfTreatmentRisk(reportEvaluationRiskSubmit);
                log.info("Successfully saved Report Evaluation Of Treatment Risk with ID: {}",reportEvaluationRiskSaved.getIdReportTreatment() );
                return ResponseEntity.status(HttpStatus.CREATED).body(reportEvaluationRiskSaved);
            } catch (Exception e){
                log.error("Error saving Report Evaluation Of Treatment Risk: {}", e.getMessage());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An error occurred while saving Report Evaluation Of Treatment Risk.:"+e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Update Report Evaluation Of Treatment Risk",
               description = "Update an existing Report Evaluation Of Treatment Risk in Phase 7 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully updated Report Evaluation Of Treatment Risk",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReportEvaluationOfTreatmentRisk.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Report Evaluation Of Treatment Risk not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updateReportEvaluationOfTreatmentRisk(@Valid @RequestBody ReportEvaluationOfTreatmentRiskDTO reportEvaluationRiskUpdate,
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
            ReportEvaluationOfTreatmentRiskDTO reportEvaluationRiskUpdated = reportEvaluationRiskService.updateReportEvaluationOfTreatmentRisk(reportEvaluationRiskUpdate);
            log.info("Successfully updated Report Evaluation Of Treatment Risk with ID: {}", reportEvaluationRiskUpdated.getIdReportTreatment());
            return ResponseEntity.status(HttpStatus.OK).body(reportEvaluationRiskUpdated);
        } catch (Exception e){
            log.error("Error updating Report Evaluation Of Treatment Risk with ID {}: {}", reportEvaluationRiskUpdate.getIdReportTreatment(), e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while updating Report Evaluation Of Treatment Risk with ID: " + reportEvaluationRiskUpdate.getIdReportTreatment());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @Operation(summary = "Delete Report Evaluation Of Treatment Risk",
               description = "Delete an existing Report Evaluation Of Treatment Risk in Phase 7 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully deleted Report Evaluation Of Treatment Risk",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReportEvaluationOfTreatmentRisk.class))),
        @ApiResponse(responseCode = "404", description = "Report Evaluation Of Treatment Risk not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{idReportTreatment}")
    public ResponseEntity<?> deleteSecurityPolicies(
        @Parameter(description = "ID of the Report Evaluation Of Treatment Risk to be deleted", required = true)
        @PathVariable UUID idReportTreatment){
        try{
            reportEvaluationRiskService.deleteReportEvaluationOfTreatmentRisk(idReportTreatment);
            log.info("Successfully deleted Report Evaluation Of Treatment Risk with ID: {}", idReportTreatment);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Report Evaluation Of Treatment Risk with ID: " + idReportTreatment + " has been deleted.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Error deleting Report Evaluation Of Treatment Risk with ID {}: {}", idReportTreatment, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while deleting Report Evaluation Of Treatment Risk with ID: " + idReportTreatment);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
