package com.espe.ListoEgsi.controller.inplantation.phase8;

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

import com.espe.ListoEgsi.domain.dto.Implantation.phase8.DeclarationAplicationSoaDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase8.DeclarationAplicationSoa;
import com.espe.ListoEgsi.service.Inplantation.phase8.DeclarationAplicationSoaService;

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
@Tag(name = "Declaration Aplication Soa", description = "Controller for managing Declaration Aplication Soa operations")
@RequestMapping("/declarationSOA")
@RestController
public class DeclarationAplicationSoaController {

    @Autowired
    private DeclarationAplicationSoaService declarationSoaService;

    /**
     * Gets the Report Evaluation Of Treatment Risk Service.
     * @param reportEvaluationOfTreatmentRiskService the service to set
     * @return the Report Evaluation Of Treatment Risk Service
     */

    @Operation(summary = "Get Declaration Aplication Soa by ID", 
               description = "Retrieves a Declaration Aplication Soa by its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of Declaration Aplication Soa",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DeclarationAplicationSoa.class))),
        @ApiResponse(responseCode = "404", description = "Declaration Aplication Soa not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))     
    })
    @GetMapping
    public ResponseEntity<?> getDeclarationAplicationSoa() {
        log.info("Received request to get Declaration Aplication Soa");
        List<DeclarationAplicationSoaDTO> declarationSoas = declarationSoaService.findAllDeclarationAplicationSoas();

        Map<String, Object> response = new HashMap<>();
        response.put("declarationSoas", declarationSoas);
        response.put("totalItems", declarationSoas.size());
        log.info("Successfully retrieved {} Declaration Aplication Soas", declarationSoas.size());
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Get Declaration Aplication Soa by ID", 
               description = "Retrieves a Declaration Aplication Soa by its unique identifier.")
               @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Declaration Aplication Soa",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DeclarationAplicationSoa.class))),   
        @ApiResponse(responseCode = "404", description = "Declaration Aplication Soa not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idDeclarationAplicationSoa}")
    public ResponseEntity<?> getDeclarationAplicationSoaById(
        @Parameter(description = "ID of the Declaration Aplication Soa to be fetched", required = true)
        @PathVariable UUID idDeclarationAplicationSoa){
        try {
            log.info("Received request to fetch Declaration Aplication Soa with ID: {}", idDeclarationAplicationSoa);
            DeclarationAplicationSoaDTO declarationAplicationSoa = declarationSoaService.getDeclarationAplicationSoa(idDeclarationAplicationSoa);
            log.info("Successfully retrieved Declaration Aplication Soa with ID: {}", idDeclarationAplicationSoa);
            return ResponseEntity.status(HttpStatus.OK).body(declarationAplicationSoa);
        }  catch (Exception e) {
            log.error("Internal server error while fetching Declaration Aplication Soa with ID: {}: {}", idDeclarationAplicationSoa, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Declaration Aplication Soa not found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Create Declaration Aplication Soa", 
               description = "Creates a new Declaration Aplication Soa associated with a specific Process ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Declaration Aplication Soa",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DeclarationAplicationSoa.class))),
        @ApiResponse(responseCode = "400", description = "Bad request, invalid ",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))     
    })
    @PostMapping
    public ResponseEntity<?> createDeclarationAplicationSoa(@Valid @RequestBody DeclarationAplicationSoaDTO declarationAplicationSoaSubmit,
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
                DeclarationAplicationSoaDTO declarationAplicationSoaSaved = declarationSoaService.saveDeclarationAplicationSoa(declarationAplicationSoaSubmit);
                log.info("Successfully saved Declaration Aplication Soa with ID: {}", declarationAplicationSoaSaved.getIdDeclaration());
                return ResponseEntity.status(HttpStatus.CREATED).body(declarationAplicationSoaSaved);
            } catch (Exception e){
                log.error("Error saving Declaration Aplication Soa: {}", e.getMessage());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An error occurred while saving Declaration Aplication Soa.:"+e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Update Declaration Aplication Soa",
               description = "Update an existing Declaration Aplication Soa in Phase 7 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully updated Declaration Aplication Soa",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = DeclarationAplicationSoa.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Declaration Aplication Soa not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updateDeclarationAplicationSoa(@Valid @RequestBody DeclarationAplicationSoaDTO declarationAplicationSoaUpdate,
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
            DeclarationAplicationSoaDTO declarationAplicationSoaUpdated = declarationSoaService.updateDeclarationAplicationSoa(declarationAplicationSoaUpdate);
            log.info("Successfully updated Declaration Aplication Soa with ID: {}", declarationAplicationSoaUpdated.getIdDeclaration());
            return ResponseEntity.status(HttpStatus.OK).body(declarationAplicationSoaUpdated);
        } catch (Exception e){
            log.error("Error updating Declaration Aplication Soa with ID {}: {}", declarationAplicationSoaUpdate.getIdDeclaration(), e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while updating Declaration Aplication Soa with ID: " + declarationAplicationSoaUpdate.getIdDeclaration());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @Operation(summary = "Delete Declaration Aplication Soa",
               description = "Delete an existing Declaration Aplication Soa in Phase 7 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully deleted Declaration Aplication Soa",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = DeclarationAplicationSoa.class))),
        @ApiResponse(responseCode = "404", description = "Declaration Aplication Soa not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{idDeclaration}")
    public ResponseEntity<?> deleteSecurityPolicies(
        @Parameter(description = "ID of the Declaration Aplication Soa to be deleted", required = true)
        @PathVariable UUID idDeclaration){
        try{
            declarationSoaService.deleteDeclarationAplicationSoa(idDeclaration);
            log.info("Successfully deleted Declaration Aplication Soa with ID: {}", idDeclaration);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Declaration Aplication Soa with ID: " + idDeclaration + " has been deleted.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Error deleting Declaration Aplication Soa with ID {}: {}", idDeclaration, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while deleting Declaration Aplication Soa with ID: " + idDeclaration);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
