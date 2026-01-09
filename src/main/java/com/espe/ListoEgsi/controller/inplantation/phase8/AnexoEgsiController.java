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

import com.espe.ListoEgsi.domain.dto.Implantation.phase8.AnexoEgsiDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase8.AnexoEgsi;
import com.espe.ListoEgsi.service.Inplantation.phase8.AnexoEgsiService;
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
@Tag(name = "Anexo Egsi", description = "Controller for managing Anexo Egsi operations")
@RequestMapping("/anexoEgsi")
@RestController
public class AnexoEgsiController {
    @Autowired
    private AnexoEgsiService anexoEgsiService;

    /**
     * Gets the Report Evaluation Of Treatment Risk Service.
     * @param reportEvaluationOfTreatmentRiskService the service to set
     * @return the Report Evaluation Of Treatment Risk Service
     */

    @Operation(summary = "Get Anexo Egsi by ID", 
               description = "Retrieves an Anexo Egsi by its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of Anexo Egsi",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnexoEgsi.class))),
        @ApiResponse(responseCode = "404", description = "Anexo Egsi not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))     
    })
    @GetMapping
    public ResponseEntity<?> getAnexoEgsis() {
        log.info("Received request to get Anexo Egsi");
        List<AnexoEgsiDTO> anexoEgsis = anexoEgsiService.findAllAnexoEgis();

        Map<String, Object> response = new HashMap<>();
        response.put("anexoEgsis", anexoEgsis);
        response.put("totalItems", anexoEgsis.size());
        log.info("Successfully retrieved {} Anexo Egsis", anexoEgsis.size());
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Get Anexo Egsi by ID", 
               description = "Retrieves an Anexo Egsi by its unique identifier.")
               @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Anexo Egsi",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnexoEgsiDTO.class))),   
        @ApiResponse(responseCode = "404", description = "Anexo Egsi not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idAnexoEgsi}")
    public ResponseEntity<?> getAnexoEgsiById(
        @Parameter(description = "ID of the Anexo Egsi to be fetched", required = true)
        @PathVariable UUID idAnexoEgsi){
        try {
            log.info("Received request to fetch Anexo Egsi with ID: {}", idAnexoEgsi);
            AnexoEgsiDTO anexoEgsi = anexoEgsiService.getAnexoEgsi(idAnexoEgsi);
            log.info("Successfully retrieved Anexo Egsi with ID: {}", idAnexoEgsi);
            return ResponseEntity.status(HttpStatus.OK).body(anexoEgsi);
        }  catch (Exception e) {
            log.error("Internal server error while fetching Anexo Egsi with ID: {}: {}", idAnexoEgsi, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Anexo Egsi not found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Create Anexo Egsi", 
               description = "Creates a new Anexo Egsi associated with a specific Process ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Anexo Egsi",
                content =  @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnexoEgsi.class))),
        @ApiResponse(responseCode = "400", description = "Bad request, invalid ",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))     
    })
    @PostMapping
    public ResponseEntity<?> createAnexoEgsi(@Valid @RequestBody AnexoEgsiDTO anexoEgsiSubmit,
        BindingResult bindingResult) {
            System.out.println("anexoEgsiSubmit:"+anexoEgsiSubmit);
            if(bindingResult.hasErrors()){
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                    log.error("Validation error on field {}: {}", error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }
            try{
                AnexoEgsiDTO anexoEgsiSaved = anexoEgsiService.saveAnexoEgsi(anexoEgsiSubmit);
                log.info("Successfully saved Anexo Egsi with ID: {}", anexoEgsiSaved.getIdAnexoEgsi());
                return ResponseEntity.status(HttpStatus.CREATED).body(anexoEgsiSaved);
            } catch (Exception e){
                log.error("Error saving Anexo Egsi: {}", e.getMessage());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An error occurred while saving Anexo Egsi.:"+e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Update Anexo Egsi",
               description = "Update an existing Anexo Egsi in Phase 8 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully updated Anexo Egsi",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AnexoEgsi.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Anexo Egsi not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updateAnexoEgsi(@Valid @RequestBody AnexoEgsiDTO anexoEgsiUpdate,
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
            AnexoEgsiDTO anexoEgsiUpdated = anexoEgsiService.updateAnexoEgsi(anexoEgsiUpdate);
            log.info("Successfully updated Anexo Egsi with ID: {}", anexoEgsiUpdated.getIdAnexoEgsi());
            return ResponseEntity.status(HttpStatus.OK).body(anexoEgsiUpdated);
        } catch (Exception e){
            log.error("Error updating Anexo Egsi with ID {}: {}", anexoEgsiUpdate.getIdAnexoEgsi(), e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while updating Anexo Egsi with ID: " + anexoEgsiUpdate.getIdAnexoEgsi());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @Operation(summary = "Delete Anexo Egsi",
               description = "Delete an existing Anexo Egsi in Phase 8 of Inplantation")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Successfully deleted Anexo Egsi",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AnexoEgsi.class))),
        @ApiResponse(responseCode = "404", description = "Anexo Egsi not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{idAnexoEgsi}")
    public ResponseEntity<?> deleteSecurityPolicies(
        @Parameter(description = "ID of the Anexo Egsi to be deleted", required = true)
        @PathVariable UUID idAnexoEgsi){
        try{
            anexoEgsiService.deleteAnexoEgsi(idAnexoEgsi);
            log.info("Successfully deleted Anexo Egsi with ID: {}", idAnexoEgsi);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Anexo Egsi with ID: " + idAnexoEgsi + " has been deleted.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Error deleting Anexo Egsi with ID {}: {}", idAnexoEgsi, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while deleting Anexo Egsi with ID: " + idAnexoEgsi);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
