package com.espe.ListoEgsi.controller.phase;

import com.espe.ListoEgsi.domain.dto.phase.*;
import com.espe.ListoEgsi.service.phase.EgsiPhaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v3/egsi/phases")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "EGSI Phases", description = "API para gestión de Fases EGSI estándar")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EgsiPhaseController {

    private final EgsiPhaseService phaseService;

    // ============ GET ALL PHASES ============
    @GetMapping
    @Operation(summary = "Obtener todas las fases EGSI", description = "Retorna todas las fases con sus secciones y preguntas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fases obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EgsiPhasesResponseDTO> getAllPhases() {
        log.info("GET /api/v3/egsi/phases - Obteniendo todas las fases");
        EgsiPhasesResponseDTO response = phaseService.getAllPhases();
        return ResponseEntity.ok(response);
    }

    // ============ GET ACTIVE PHASES ONLY ============
    @GetMapping("/active")
    @Operation(summary = "Obtener fases activas", description = "Retorna solo las fases activas para uso en procesos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fases activas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EgsiPhasesResponseDTO> getActivePhases() {
        log.info("GET /api/v3/egsi/phases/active - Obteniendo fases activas");
        EgsiPhasesResponseDTO response = phaseService.getActivePhases();
        return ResponseEntity.ok(response);
    }

    // ============ GET PHASE BY ID ============
    @GetMapping("/{idPhase}")
    @Operation(summary = "Obtener fase por ID", description = "Retorna una fase específica con sus secciones y preguntas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fase encontrada"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EgsiPhaseDTO> getPhaseById(
            @Parameter(description = "ID de la fase") @PathVariable String idPhase) {
        log.info("GET /api/v3/egsi/phases/{} - Obteniendo fase", idPhase);
        EgsiPhaseDTO response = phaseService.getPhaseById(idPhase);
        return ResponseEntity.ok(response);
    }

    // ============ GET STATISTICS ============
    @GetMapping("/statistics")
    @Operation(summary = "Obtener estadísticas", description = "Retorna estadísticas de fases, secciones y preguntas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
    })
    public ResponseEntity<EgsiPhasesResponseDTO> getStatistics() {
        log.info("GET /api/v3/egsi/phases/statistics - Obteniendo estadísticas");
        EgsiPhasesResponseDTO response = phaseService.getStatistics();
        return ResponseEntity.ok(response);
    }

    // ============ CREATE PHASE ============
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nueva fase", description = "Crea una nueva fase EGSI con sus secciones y preguntas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Fase creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EgsiPhaseDTO> createPhase(
            @Valid @RequestBody CreatePhaseRequestDTO request) {
        log.info("POST /api/v3/egsi/phases - Creando fase: {}", request.getTitle());
        EgsiPhaseDTO response = phaseService.createPhase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============ SAVE ALL PHASES (REPLACE ALL) ============
    @PostMapping("/save-all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Guardar todas las fases", description = "Reemplaza todas las fases existentes con las nuevas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fases guardadas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EgsiPhasesResponseDTO> saveAllPhases(
            @Valid @RequestBody SaveAllPhasesRequestDTO request) {
        log.info("POST /api/v3/egsi/phases/save-all - Guardando {} fases", request.getPhases().size());
        EgsiPhasesResponseDTO response = phaseService.saveAllPhases(request);
        return ResponseEntity.ok(response);
    }

    // ============ UPDATE PHASE ============
    @PutMapping("/{idPhase}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar fase", description = "Actualiza una fase existente con sus secciones y preguntas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fase actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EgsiPhaseDTO> updatePhase(
            @Parameter(description = "ID de la fase") @PathVariable String idPhase,
            @Valid @RequestBody CreatePhaseRequestDTO request) {
        log.info("PUT /api/v3/egsi/phases/{} - Actualizando fase", idPhase);
        EgsiPhaseDTO response = phaseService.updatePhase(idPhase, request);
        return ResponseEntity.ok(response);
    }

    // ============ TOGGLE PHASE ACTIVE ============
    @PatchMapping("/{idPhase}/toggle-active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activar/Desactivar fase", description = "Cambia el estado activo de una fase")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado cambiado exitosamente"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EgsiPhaseDTO> togglePhaseActive(
            @Parameter(description = "ID de la fase") @PathVariable String idPhase) {
        log.info("PATCH /api/v3/egsi/phases/{}/toggle-active - Cambiando estado", idPhase);
        EgsiPhaseDTO response = phaseService.togglePhaseActive(idPhase);
        return ResponseEntity.ok(response);
    }

    // ============ DELETE PHASE ============
    @DeleteMapping("/{idPhase}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar fase", description = "Elimina una fase y todo su contenido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fase eliminada exitosamente"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Map<String, String>> deletePhase(
            @Parameter(description = "ID de la fase") @PathVariable String idPhase) {
        log.info("DELETE /api/v3/egsi/phases/{} - Eliminando fase", idPhase);
        phaseService.deletePhase(idPhase);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Fase eliminada exitosamente");
        response.put("idPhase", idPhase);
        
        return ResponseEntity.ok(response);
    }

    // ============ ADD SECTION TO PHASE ============
    @PostMapping("/{idPhase}/sections")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agregar sección a fase", description = "Agrega una nueva sección a una fase existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sección agregada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EgsiSectionDTO> addSectionToPhase(
            @Parameter(description = "ID de la fase") @PathVariable String idPhase,
            @Valid @RequestBody CreateSectionRequestDTO request) {
        log.info("POST /api/v3/egsi/phases/{}/sections - Agregando sección", idPhase);
        EgsiSectionDTO response = phaseService.addSectionToPhase(idPhase, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============ ADD QUESTION TO SECTION ============
    @PostMapping("/sections/{idSection}/questions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agregar pregunta a sección", description = "Agrega una nueva pregunta a una sección existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pregunta agregada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Sección no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EgsiQuestionDTO> addQuestionToSection(
            @Parameter(description = "ID de la sección") @PathVariable String idSection,
            @Valid @RequestBody CreateQuestionRequestDTO request) {
        log.info("POST /api/v3/egsi/phases/sections/{}/questions - Agregando pregunta", idSection);
        EgsiQuestionDTO response = phaseService.addQuestionToSection(idSection, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============ DELETE SECTION ============
    @DeleteMapping("/sections/{idSection}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar sección", description = "Elimina una sección y todas sus preguntas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sección eliminada exitosamente"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Sección no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Map<String, String>> deleteSection(
            @Parameter(description = "ID de la sección") @PathVariable String idSection) {
        log.info("DELETE /api/v3/egsi/phases/sections/{} - Eliminando sección", idSection);
        phaseService.deleteSection(idSection);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Sección eliminada exitosamente");
        response.put("idSection", idSection);
        
        return ResponseEntity.ok(response);
    }

    // ============ DELETE QUESTION ============
    @DeleteMapping("/questions/{idQuestion}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar pregunta", description = "Elimina una pregunta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pregunta eliminada exitosamente"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Pregunta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Map<String, String>> deleteQuestion(
            @Parameter(description = "ID de la pregunta") @PathVariable String idQuestion) {
        log.info("DELETE /api/v3/egsi/phases/questions/{} - Eliminando pregunta", idQuestion);
        phaseService.deleteQuestion(idQuestion);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pregunta eliminada exitosamente");
        response.put("idQuestion", idQuestion);
        
        return ResponseEntity.ok(response);
    }
}
