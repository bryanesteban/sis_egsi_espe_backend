package com.espe.ListoEgsi.controller.phase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.espe.ListoEgsi.domain.dto.phase.CreateApprovalRequestDTO;
import com.espe.ListoEgsi.domain.dto.phase.PhaseApprovalDTO;
import com.espe.ListoEgsi.domain.dto.phase.ReviewApprovalRequestDTO;
import com.espe.ListoEgsi.service.phase.PhaseApprovalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para gestión de solicitudes de aprobación de fases.
 * Permite a los usuarios solicitar aprobación cuando completan una fase
 * y a los aprobadores revisar y aprobar/rechazar las solicitudes.
 */
@RestController
@RequestMapping("/api/v3/phase-approvals")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Aprobaciones de Fases", description = "API para gestión de solicitudes de aprobación de fases EGSI")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PhaseApprovalController {

    private final PhaseApprovalService approvalService;

    // ============ CREAR SOLICITUD DE APROBACIÓN ============
    @PostMapping
    @Operation(
        summary = "Solicitar aprobación de fase",
        description = "Crea una nueva solicitud de aprobación para una fase completada"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente",
            content = @Content(schema = @Schema(implementation = PhaseApprovalDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o ya existe solicitud pendiente"),
        @ApiResponse(responseCode = "404", description = "Proceso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> createApprovalRequest(
            @Valid @RequestBody CreateApprovalRequestDTO request,
            BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            PhaseApprovalDTO approval = approvalService.createApprovalRequest(request);
            log.info("POST /api/v3/phase-approvals - Solicitud creada: {}", approval.getIdApproval());
            return ResponseEntity.status(HttpStatus.CREATED).body(approval);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("Error creating approval request", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ============ REVISAR SOLICITUD (APROBAR/RECHAZAR) ============
    @PutMapping("/review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('APPROVER')")
    @Operation(
        summary = "Revisar solicitud de aprobación",
        description = "Permite a un aprobador aprobar o rechazar una solicitud pendiente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud revisada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o solicitud ya procesada"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<?> reviewApprovalRequest(
            @Valid @RequestBody ReviewApprovalRequestDTO request,
            BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            PhaseApprovalDTO approval = approvalService.reviewApprovalRequest(request);
            log.info("PUT /api/v3/phase-approvals/review - Solicitud {} revisada como {}", 
                approval.getIdApproval(), approval.getStatus());
            return ResponseEntity.ok(approval);
        } catch (IllegalStateException | IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("Error reviewing approval request", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al revisar solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ============ CANCELAR SOLICITUD ============
    @DeleteMapping("/{idApproval}")
    @Operation(
        summary = "Cancelar solicitud de aprobación",
        description = "Permite al usuario cancelar su solicitud pendiente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud cancelada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud ya procesada"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<?> cancelApprovalRequest(
            @Parameter(description = "ID de la solicitud") @PathVariable UUID idApproval,
            @Parameter(description = "Usuario que cancela") @RequestParam String cancelledBy) {
        
        try {
            PhaseApprovalDTO approval = approvalService.cancelApprovalRequest(idApproval, cancelledBy);
            log.info("DELETE /api/v3/phase-approvals/{} - Solicitud cancelada", idApproval);
            return ResponseEntity.ok(approval);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("Error cancelling approval request", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al cancelar solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ============ OBTENER SOLICITUDES PENDIENTES ============
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('APPROVER')")
    @Operation(
        summary = "Obtener solicitudes pendientes",
        description = "Lista todas las solicitudes de aprobación pendientes de revisión"
    )
    public ResponseEntity<?> getPendingApprovals() {
        try {
            List<PhaseApprovalDTO> approvals = approvalService.getPendingApprovals();
            
            Map<String, Object> response = new HashMap<>();
            response.put("approvals", approvals);
            response.put("total", approvals.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting pending approvals", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener solicitudes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ============ OBTENER SOLICITUDES POR PROCESO ============
    @GetMapping("/process/{idProcess}")
    @Operation(
        summary = "Obtener solicitudes por proceso",
        description = "Lista todas las solicitudes de un proceso específico"
    )
    public ResponseEntity<?> getApprovalsByProcess(
            @Parameter(description = "ID del proceso") @PathVariable UUID idProcess) {
        try {
            List<PhaseApprovalDTO> approvals = approvalService.getApprovalsByProcess(idProcess);
            
            Map<String, Object> response = new HashMap<>();
            response.put("approvals", approvals);
            response.put("total", approvals.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting approvals by process", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener solicitudes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ============ OBTENER UNA SOLICITUD ============
    @GetMapping("/{idApproval}")
    @Operation(
        summary = "Obtener solicitud por ID",
        description = "Obtiene los detalles de una solicitud específica"
    )
    public ResponseEntity<?> getApprovalById(
            @Parameter(description = "ID de la solicitud") @PathVariable UUID idApproval) {
        try {
            PhaseApprovalDTO approval = approvalService.getApprovalById(idApproval);
            return ResponseEntity.ok(approval);
        } catch (Exception e) {
            log.error("Error getting approval by id", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ============ VERIFICAR SI HAY SOLICITUD PENDIENTE ============
    @GetMapping("/check/{idProcess}/{idPhase}")
    @Operation(
        summary = "Verificar solicitud pendiente",
        description = "Verifica si existe una solicitud pendiente para un proceso y fase"
    )
    public ResponseEntity<?> checkPendingApproval(
            @Parameter(description = "ID del proceso") @PathVariable UUID idProcess,
            @Parameter(description = "ID de la fase") @PathVariable String idPhase) {
        try {
            boolean hasPending = approvalService.hasPendingApproval(idProcess, idPhase);
            PhaseApprovalDTO lastApproval = approvalService.getLastApprovalForPhase(idProcess, idPhase);
            
            Map<String, Object> response = new HashMap<>();
            response.put("hasPendingApproval", hasPending);
            response.put("lastApproval", lastApproval);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking pending approval", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al verificar solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ============ OBTENER HISTORIAL ============
    @GetMapping("/history/{idProcess}")
    @Operation(
        summary = "Obtener historial de aprobaciones",
        description = "Lista el historial completo de aprobaciones de un proceso"
    )
    public ResponseEntity<?> getApprovalHistory(
            @Parameter(description = "ID del proceso") @PathVariable UUID idProcess) {
        try {
            List<PhaseApprovalDTO> history = approvalService.getApprovalHistory(idProcess);
            
            Map<String, Object> response = new HashMap<>();
            response.put("history", history);
            response.put("total", history.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting approval history", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener historial: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ============ CONTAR SOLICITUDES PENDIENTES ============
    @GetMapping("/count/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('APPROVER')")
    @Operation(
        summary = "Contar solicitudes pendientes",
        description = "Retorna el número de solicitudes pendientes de revisión"
    )
    public ResponseEntity<?> countPendingApprovals() {
        try {
            long count = approvalService.countPendingApprovals();
            
            Map<String, Object> response = new HashMap<>();
            response.put("pendingCount", count);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error counting pending approvals", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al contar solicitudes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
