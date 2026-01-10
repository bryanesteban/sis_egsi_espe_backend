package com.espe.ListoEgsi.controller.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.espe.ListoEgsi.domain.dto.question.PhaseDTO;
import com.espe.ListoEgsi.service.question.PhaseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para gestión de fases de procesos EGSI.
 * Administra las diferentes etapas de implementación de cada proceso.
 * 
 * @version 1.0
 * @since 2026-01-10
 */
@Slf4j
@RestController
@RequestMapping("/api/phases")
@RequiredArgsConstructor
@Tag(name = "Fases", description = "API para gestión de fases de implementación de procesos EGSI")
@SecurityRequirement(name = "bearerAuth")
public class PhaseController {

    private final PhaseService phaseService;

    @PostMapping
    @Operation(
        summary = "Crear nueva fase",
        description = "Crea una nueva fase de implementación para un proceso EGSI con fechas y orden específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Fase creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> createPhase(
            @Parameter(description = "Datos de la nueva fase", required = true)
            @Valid @RequestBody PhaseDTO phaseDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in create phase request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        PhaseDTO createdPhase = phaseService.createPhase(phaseDTO);
        return new ResponseEntity<>(createdPhase, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar una fase existente",
        description = "Modifica los datos de una fase de implementación existente incluyendo fechas, estado y cuestionarios asociados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fase actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> updatePhase(
            @Parameter(description = "UUID de la fase a actualizar", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "Datos actualizados de la fase", required = true)
            @Valid @RequestBody PhaseDTO phaseDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in update phase request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        PhaseDTO updatedPhase = phaseService.updatePhase(id, phaseDTO);
        return ResponseEntity.ok(updatedPhase);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar una fase",
        description = "Elimina permanentemente una fase del sistema. Esta acción no se puede deshacer."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Fase eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Void> deletePhase(
            @Parameter(description = "UUID de la fase a eliminar", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        phaseService.deletePhase(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener una fase por ID",
        description = "Recupera los detalles completos de una fase específica por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fase encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<PhaseDTO> getPhaseById(
            @Parameter(description = "UUID de la fase", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        PhaseDTO phase = phaseService.getPhaseById(id);
        return ResponseEntity.ok(phase);
    }

    @GetMapping
    @Operation(
        summary = "Obtener todas las fases",
        description = "Recupera una lista completa de todas las fases de implementación registradas en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de fases recuperada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<PhaseDTO>> getAllPhases() {
        List<PhaseDTO> phases = phaseService.getAllPhases();
        return ResponseEntity.ok(phases);
    }

    @GetMapping("/process/{idProcess}")
    @Operation(
        summary = "Obtener fases por proceso",
        description = "Recupera todas las fases asociadas a un proceso específico ordenadas por secuencia"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de fases del proceso recuperada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proceso no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<PhaseDTO>> getPhasesByProcess(
            @Parameter(description = "UUID del proceso", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID idProcess) {
        List<PhaseDTO> phases = phaseService.getPhasesByProcess(idProcess);
        return ResponseEntity.ok(phases);
    }

    @GetMapping("/status/{status}")
    @Operation(
        summary = "Obtener fases por estado",
        description = "Filtra las fases según su estado actual (pendiente, en progreso, completada, etc.)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de fases filtradas por estado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<PhaseDTO>> getPhasesByStatus(
            @Parameter(description = "Estado de la fase", required = true, example = "en_progreso")
            @PathVariable String status) {
        List<PhaseDTO> phases = phaseService.getPhasesByStatus(status);
        return ResponseEntity.ok(phases);
    }
}
