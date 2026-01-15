package com.espe.ListoEgsi.controller.phase;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.espe.ListoEgsi.service.phase.PhaseReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para generación de reportes PDF de fases aprobadas.
 * Permite descargar un informe completo de la fase cuando ha sido aprobada.
 */
@RestController
@RequestMapping("/api/v3/phase-reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reportes de Fases", description = "API para generación de reportes PDF de fases EGSI")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PhaseReportController {

    private final PhaseReportService reportService;

    /**
     * Genera y descarga el reporte PDF de una fase aprobada.
     * El PDF incluye toda la información de la fase, respuestas y datos de aprobación.
     *
     * @param processId ID del proceso
     * @param phaseId ID de la fase
     * @return PDF como byte array
     */
    @GetMapping("/{processId}/{phaseId}/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'APPROVER', 'USER')")
    @Operation(
        summary = "Generar reporte PDF de fase",
        description = "Genera un reporte PDF completo de una fase aprobada, incluyendo todas las respuestas y la información de aprobación"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PDF generado exitosamente"),
        @ApiResponse(responseCode = "400", description = "La fase no está aprobada"),
        @ApiResponse(responseCode = "404", description = "Proceso o fase no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error al generar el PDF")
    })
    public ResponseEntity<byte[]> generatePhaseReport(
            @Parameter(description = "ID del proceso", required = true)
            @PathVariable UUID processId,
            @Parameter(description = "ID de la fase (ej: 5.1, 6.1.1)", required = true)
            @PathVariable String phaseId) {
        
        log.info("Generando reporte PDF para proceso {} y fase {}", processId, phaseId);
        
        try {
            byte[] pdfContent = reportService.generatePhaseReport(processId, phaseId);
            
            // Crear nombre de archivo
            String filename = String.format("Reporte_Fase_%s_Proceso_%s.pdf", 
                phaseId.replace(".", "_"), 
                processId.toString().substring(0, 8));
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            headers.setContentLength(pdfContent.length);
            
            log.info("Reporte PDF generado exitosamente: {} ({} bytes)", filename, pdfContent.length);
            
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
            
        } catch (IllegalStateException e) {
            log.warn("No se puede generar reporte: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            log.warn("Proceso o fase no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al generar reporte PDF", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Previsualiza el reporte PDF de una fase aprobada (inline en navegador).
     *
     * @param processId ID del proceso
     * @param phaseId ID de la fase
     * @return PDF como byte array para visualización
     */
    @GetMapping("/{processId}/{phaseId}/preview")
    @PreAuthorize("hasAnyRole('ADMIN', 'APPROVER', 'USER')")
    @Operation(
        summary = "Previsualizar reporte PDF de fase",
        description = "Previsualiza el reporte PDF en el navegador sin descargarlo"
    )
    public ResponseEntity<byte[]> previewPhaseReport(
            @PathVariable UUID processId,
            @PathVariable String phaseId) {
        
        log.info("Previsualizando reporte PDF para proceso {} y fase {}", processId, phaseId);
        
        try {
            byte[] pdfContent = reportService.generatePhaseReport(processId, phaseId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            headers.setContentLength(pdfContent.length);
            
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
            
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al previsualizar reporte PDF", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
