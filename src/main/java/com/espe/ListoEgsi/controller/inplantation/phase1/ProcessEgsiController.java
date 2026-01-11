package com.espe.ListoEgsi.controller.inplantation.phase1;

import java.io.ObjectInputFilter.Status;
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
import com.espe.ListoEgsi.domain.dto.question.AnswerDTO;
import com.espe.ListoEgsi.domain.dto.question.PhaseDTO;
import com.espe.ListoEgsi.domain.model.entity.question.Answer;
import com.espe.ListoEgsi.enums.PhaseEnum;
import com.espe.ListoEgsi.enums.StatusEnum;
import com.espe.ListoEgsi.service.Inplantation.phase1.ProcessService;
import com.espe.ListoEgsi.service.question.AnswerService;
import com.espe.ListoEgsi.service.question.PhaseService;

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
@Tag(name = "Procesos EGSI", description = "API para gestión de procesos de implementación EGSI") 
@RequestMapping("/processEgsi")
@RestController
public class ProcessEgsiController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private AnswerService answerService;

    /**
     * Get all available processes
     * @return List of all active processes
     */



    @Operation(
        summary = "Obtener todos los procesos",
        description = "Recupera una lista completa de todos los procesos de implementación EGSI registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de procesos recuperada exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProcessEgsiDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
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


    @Operation(
        summary = "Crear nuevo proceso EGSI",
        description = "Registra un nuevo proceso de implementación EGSI con nombre, descripción, fechas y estado inicial"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Proceso creado exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProcessEgsiDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos - Verifique campos requeridos",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> saveNewProcess(
        @Parameter(description = "Datos del nuevo proceso EGSI", required = true)
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
            PhaseDTO  phaseforCreate =  PhaseDTO.builder()
                                                    .idProcess(savedProcess.getIdProcess())
                                                    .questionaryCode(PhaseEnum.FASE1.getQuestionnaireCode())
                                                    .responsibles(processSave.getCreatedBy())
                                                    .status(StatusEnum.ACTIVE.name())
                                                    .build();
            PhaseDTO initialPhase = phaseService.createPhase(phaseforCreate);
            log.info("Initial phase created for process ID: {}", savedProcess.getIdProcess());

            List<AnswerDTO> initialAnswers = answerService.createAnswersByPhase(initialPhase.getIdPhase(), PhaseEnum.FASE1.getQuestionnaireCode());
            log.info("Initial answers created for phase ID: {}", initialPhase.getIdPhase());
            Map<String, Object> response = new HashMap<>();
            response.put("process", savedProcess);
            response.put("initialPhase", initialPhase);
            response.put("initialAnswersCount", initialAnswers.size());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error submitting process with ID: {}", processSave.getIdProcess(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error saving process: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(
        summary = "Buscar proceso por ID",
        description = "Recupera los detalles completos de un proceso EGSI específico por su identificador único (UUID)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proceso encontrado exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProcessEgsiDTO.class))),
        @ApiResponse(responseCode = "404", description = "Proceso no encontrado",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("find/{idProcess}")
    public ResponseEntity<?> getProcessEgsiById(
        @Parameter(description = "UUID del proceso a buscar", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
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

    @Operation(
        summary = "Actualizar proceso existente",
        description = "Modifica los datos de un proceso EGSI existente incluyendo nombre, descripción, fechas y estado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proceso actualizado exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProcessEgsiDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Proceso no encontrado",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<?> updateProcess(
        @Parameter(description = "Datos actualizados del proceso EGSI", required = true)
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

    @Operation(
        summary = "Eliminar proceso (soft delete)",
        description = "Elimina lógicamente un proceso EGSI por su ID. El proceso no se elimina físicamente de la base de datos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Proceso eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proceso no encontrado",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{idProcess}")
    public ResponseEntity<?> deleteProcess(
        @Parameter(description = "UUID del proceso a eliminar", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
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
