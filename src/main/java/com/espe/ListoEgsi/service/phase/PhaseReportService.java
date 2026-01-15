package com.espe.ListoEgsi.service.phase;

import java.util.UUID;

/**
 * Servicio para generar reportes PDF de fases aprobadas.
 */
public interface PhaseReportService {
    
    /**
     * Genera un reporte PDF de una fase aprobada.
     * @param processId ID del proceso
     * @param phaseId ID de la fase
     * @return byte array con el contenido del PDF
     */
    byte[] generatePhaseReport(UUID processId, String phaseId);
}
