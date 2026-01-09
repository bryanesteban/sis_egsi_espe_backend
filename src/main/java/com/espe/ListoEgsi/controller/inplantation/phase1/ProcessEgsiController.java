package com.espe.ListoEgsi.controller.inplantation.phase1;

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

import com.espe.ListoEgsi.domain.dto.Implantation.phase1.ProcessEgsiDTO;
import com.espe.ListoEgsi.service.Inplantation.phase1.ProcessService;

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
@Tag(name = "Process Egsi", description = "API for managing processes of egsi") 
@RequestMapping("/processEgsi")
@RestController
public class ProcessEgsiController {

    @Autowired
    private ProcessService processService;

    /**
     * Get all available processes
     * @return List of all active processes
     */



    @Operation(summary = "get all processes",
               description = "Retrieve a list of all processes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of processes",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProcessEgsiDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getAllProcesses() {
        try {
            List<ProcessEgsiDTO> process = processService.findAllProcess();

            Map<String, Object> response = new HashMap<>();
            response.put("process", process);
            response.put("total", process.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error","Error retrieving process:"+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Save process of egsi",
               description = "Save a new process of egsi ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Process saved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProcessEgsiDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<Object> saveNewProcess(
        @Valid @RequestBody ProcessEgsiDTO processSave,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in submit process request: {}", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            ProcessEgsiDTO savedProcess = processService.saveProcess(processSave);
            log.info("Successfully submitted process with ID: {}", savedProcess.getIdProcess());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProcess);
        } catch (Exception e) {
            log.error("Error submitting process with ID: {}", processSave.getIdProcess(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error saving process: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "find process by idProcess",
               description = "find process by idProcess ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Process found successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProcessEgsiDTO.class))),
        @ApiResponse(responseCode = "404", description = "Process not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idProcess}")
    public ResponseEntity<?> getProcessEgsiById(
        @Parameter(description = "ID of the process to retrieve", required = true)
        @PathVariable UUID idProcess) {
        try {

            ProcessEgsiDTO process = processService.getProcess(idProcess);
            return ResponseEntity.status(HttpStatus.OK).body(process);

        } catch (Exception e) {

            log.error("Error retrieving process with ID: {}", idProcess, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error retrieving process: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "update process",
               description = "Update an existing process ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Process updated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProcessEgsiDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Process not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updateProcess(
        @Valid @RequestBody ProcessEgsiDTO processUpdate,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in update process request: {}", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            ProcessEgsiDTO updatedProcess = processService.updateProcess(processUpdate);
            log.info("Successfully updated process with ID: {}", updatedProcess.getIdProcess());
            return ResponseEntity.ok(updatedProcess);
        } catch (Exception e) {
            log.error("Error updating process with ID: {}", processUpdate.getIdProcess(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error updating process: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Delete process",
               description = "Soft delete an existing process by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Process deleted successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProcessEgsiDTO.class))),
        @ApiResponse(responseCode = "404", description = "Process not found",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{idProcess}")
    public ResponseEntity<?> deleteProcess(
        @Parameter(description = "ID of the process to delete", required = true)
        @PathVariable UUID idProcess) {
            log.info("Request to delete process with ID: {}", idProcess);
        try {
            processService.deleteProcess(idProcess);
            log.info("Successfully deleted process with ID: {}", idProcess);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting process with ID: {}", idProcess, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error deleting process: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
