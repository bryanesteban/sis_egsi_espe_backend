package com.espe.ListoEgsi.service.phase.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espe.ListoEgsi.domain.dto.Implantation.phase1.ProcessEgsiDTO;
import com.espe.ListoEgsi.domain.dto.phase.CreateApprovalRequestDTO;
import com.espe.ListoEgsi.domain.dto.phase.PhaseApprovalDTO;
import com.espe.ListoEgsi.domain.dto.phase.ReviewApprovalRequestDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.question.PhaseApprovalRequest;
import com.espe.ListoEgsi.enums.PhaseEnum;
import com.espe.ListoEgsi.exception.ResourceNotFoundException;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.phase.PhaseApprovalRepository;
import com.espe.ListoEgsi.service.phase.PhaseApprovalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PhaseApprovalServiceImpl implements PhaseApprovalService {

    private final PhaseApprovalRepository approvalRepository;
    private final ProcessRepository processRepository;

    @Override
    public PhaseApprovalDTO createApprovalRequest(CreateApprovalRequestDTO request) {
        log.info("Creating approval request for process {} phase {}", request.getIdProcess(), request.getIdPhase());

        // Validar que idPhase no sea null o vacío
        if (request.getIdPhase() == null || request.getIdPhase().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la fase es requerido");
        }

        // Verificar si ya existe una solicitud pendiente
        if (approvalRepository.existsPendingByProcessAndPhase(request.getIdProcess(), request.getIdPhase())) {
            throw new IllegalStateException("Ya existe una solicitud de aprobación pendiente para esta fase");
        }

        // Obtener el proceso
        ProcessEgsi process = processRepository.findById(request.getIdProcess())
                .orElseThrow(() -> new ResourceNotFoundException("Proceso no encontrado con ID: " + request.getIdProcess()));

        // Crear la solicitud
        PhaseApprovalRequest approval = PhaseApprovalRequest.builder()
                .process(process)
                .idPhase(request.getIdPhase())
                .phaseOrder(request.getPhaseOrder())
                .phaseTitle(request.getPhaseTitle())
                .status("PENDING")
                .requestedBy(request.getRequestedBy())
                .requestedAt(LocalDateTime.now())
                .comments(request.getComments())
                .build();

        PhaseApprovalRequest saved = approvalRepository.save(approval);
        log.info("Approval request created with ID: {}", saved.getIdApproval());

        return toDTO(saved);
    }

    @Override
    public PhaseApprovalDTO reviewApprovalRequest(ReviewApprovalRequestDTO request) {
        log.info("Reviewing approval request {} with action {}", request.getIdApproval(), request.getAction());

        PhaseApprovalRequest approval = approvalRepository.findById(request.getIdApproval())
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud de aprobación no encontrada"));

        if (!"PENDING".equals(approval.getStatus())) {
            throw new IllegalStateException("Solo se pueden revisar solicitudes pendientes");
        }

        String action = request.getAction().toUpperCase();
        if (!action.equals("APPROVED") && !action.equals("REJECTED")) {
            throw new IllegalArgumentException("Acción inválida. Debe ser APPROVED o REJECTED");
        }

        if (action.equals("REJECTED") && (request.getRejectionReason() == null || request.getRejectionReason().isBlank())) {
            throw new IllegalArgumentException("Debe proporcionar una razón de rechazo");
        }

        approval.setStatus(action);
        approval.setReviewedBy(request.getReviewedBy());
        approval.setReviewedAt(LocalDateTime.now());
        
        if (action.equals("REJECTED")) {
            approval.setRejectionReason(request.getRejectionReason());
        }

        // Si es aprobado, avanzar el proceso a la siguiente fase
        if (action.equals("APPROVED")) {
            advanceProcessToNextPhase(approval.getProcess(), approval.getPhaseOrder());
        }

        PhaseApprovalRequest saved = approvalRepository.save(approval);
        log.info("Approval request {} reviewed as {}", saved.getIdApproval(), action);

        return toDTO(saved);
    }

    /**
     * Avanza el proceso a la siguiente fase
     */
    private void advanceProcessToNextPhase(ProcessEgsi process, int currentPhaseOrder) {
        int nextPhaseOrder = currentPhaseOrder + 1;
        String nextPhaseName = "FASE" + nextPhaseOrder;
        
        // Verificar si existe la siguiente fase
        PhaseEnum nextPhase = PhaseEnum.fromPhaseName(nextPhaseName);
        if (nextPhase != null) {
            process.setCustomPhase(nextPhaseName);
            processRepository.save(process);
            log.info("Process {} advanced to phase {}", process.getIdProcess(), nextPhaseName);
        } else {
            // No hay más fases, marcar como completado
            process.setStatus("COMPLETED");
            processRepository.save(process);
            log.info("Process {} completed all phases", process.getIdProcess());
        }
    }

    @Override
    public PhaseApprovalDTO cancelApprovalRequest(UUID idApproval, String cancelledBy) {
        log.info("Cancelling approval request {} by {}", idApproval, cancelledBy);

        PhaseApprovalRequest approval = approvalRepository.findById(idApproval)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud de aprobación no encontrada"));

        if (!"PENDING".equals(approval.getStatus())) {
            throw new IllegalStateException("Solo se pueden cancelar solicitudes pendientes");
        }

        approval.setStatus("CANCELLED");
        approval.setReviewedBy(cancelledBy);
        approval.setReviewedAt(LocalDateTime.now());

        PhaseApprovalRequest saved = approvalRepository.save(approval);
        return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhaseApprovalDTO> getPendingApprovals() {
        return approvalRepository.findPendingApprovals().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhaseApprovalDTO> getApprovalsByProcess(UUID idProcess) {
        return approvalRepository.findByProcessId(idProcess).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PhaseApprovalDTO getApprovalById(UUID idApproval) {
        PhaseApprovalRequest approval = approvalRepository.findById(idApproval)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud de aprobación no encontrada"));
        return toDTO(approval);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPendingApproval(UUID idProcess, String idPhase) {
        return approvalRepository.existsPendingByProcessAndPhase(idProcess, idPhase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhaseApprovalDTO> getApprovalHistory(UUID idProcess) {
        return approvalRepository.findHistoryByProcess(idProcess).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countPendingApprovals() {
        return approvalRepository.countPending();
    }

    @Override
    @Transactional(readOnly = true)
    public PhaseApprovalDTO getLastApprovalForPhase(UUID idProcess, String idPhase) {
        List<PhaseApprovalRequest> approvals = approvalRepository.findByProcessAndPhase(idProcess, idPhase);
        if (approvals.isEmpty()) {
            return null;
        }
        return toDTO(approvals.get(0));
    }

    /**
     * Convierte una entidad a DTO
     */
    private PhaseApprovalDTO toDTO(PhaseApprovalRequest entity) {
        return PhaseApprovalDTO.builder()
                .idApproval(entity.getIdApproval())
                .idProcess(entity.getProcess().getIdProcess())
                .processName(entity.getProcess().getName())
                .idPhase(entity.getIdPhase())
                .phaseOrder(entity.getPhaseOrder())
                .phaseTitle(entity.getPhaseTitle())
                .status(entity.getStatus())
                .requestedBy(entity.getRequestedBy())
                .requestedAt(entity.getRequestedAt())
                .reviewedBy(entity.getReviewedBy())
                .reviewedAt(entity.getReviewedAt())
                .comments(entity.getComments())
                .rejectionReason(entity.getRejectionReason())
                .build();
    }
}
