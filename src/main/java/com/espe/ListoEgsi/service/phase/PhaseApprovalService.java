package com.espe.ListoEgsi.service.phase;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.phase.CreateApprovalRequestDTO;
import com.espe.ListoEgsi.domain.dto.phase.PhaseApprovalDTO;
import com.espe.ListoEgsi.domain.dto.phase.ReviewApprovalRequestDTO;

/**
 * Servicio para gestionar las solicitudes de aprobación de fases.
 */
public interface PhaseApprovalService {

    /**
     * Crear una nueva solicitud de aprobación
     */
    PhaseApprovalDTO createApprovalRequest(CreateApprovalRequestDTO request);

    /**
     * Revisar (aprobar/rechazar) una solicitud
     */
    PhaseApprovalDTO reviewApprovalRequest(ReviewApprovalRequestDTO request);

    /**
     * Cancelar una solicitud pendiente
     */
    PhaseApprovalDTO cancelApprovalRequest(UUID idApproval, String cancelledBy);

    /**
     * Obtener todas las solicitudes pendientes
     */
    List<PhaseApprovalDTO> getPendingApprovals();

    /**
     * Obtener solicitudes por proceso
     */
    List<PhaseApprovalDTO> getApprovalsByProcess(UUID idProcess);

    /**
     * Obtener una solicitud por ID
     */
    PhaseApprovalDTO getApprovalById(UUID idApproval);

    /**
     * Verificar si existe una solicitud pendiente para un proceso y fase
     */
    boolean hasPendingApproval(UUID idProcess, String idPhase);

    /**
     * Obtener el historial de aprobaciones de un proceso
     */
    List<PhaseApprovalDTO> getApprovalHistory(UUID idProcess);

    /**
     * Contar solicitudes pendientes
     */
    long countPendingApprovals();

    /**
     * Obtener la última solicitud de una fase específica (para ver estado)
     */
    PhaseApprovalDTO getLastApprovalForPhase(UUID idProcess, String idPhase);
}
