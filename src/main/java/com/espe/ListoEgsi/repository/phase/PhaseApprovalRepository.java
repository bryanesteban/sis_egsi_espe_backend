package com.espe.ListoEgsi.repository.phase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.espe.ListoEgsi.domain.model.entity.question.PhaseApprovalRequest;

/**
 * Repositorio para gestionar las solicitudes de aprobación de fases.
 */
@Repository
public interface PhaseApprovalRepository extends JpaRepository<PhaseApprovalRequest, UUID> {

    /**
     * Buscar solicitudes por proceso
     */
    @Query("SELECT p FROM PhaseApprovalRequest p WHERE p.process.idProcess = :idProcess ORDER BY p.requestedAt DESC")
    List<PhaseApprovalRequest> findByProcessId(@Param("idProcess") UUID idProcess);

    /**
     * Buscar solicitudes por estado
     */
    List<PhaseApprovalRequest> findByStatusOrderByRequestedAtDesc(String status);

    /**
     * Buscar solicitudes pendientes
     */
    @Query("SELECT p FROM PhaseApprovalRequest p WHERE p.status = 'PENDING' ORDER BY p.requestedAt ASC")
    List<PhaseApprovalRequest> findPendingApprovals();

    /**
     * Buscar solicitud pendiente por proceso y fase
     */
    @Query("SELECT p FROM PhaseApprovalRequest p WHERE p.process.idProcess = :idProcess AND p.idPhase = :idPhase AND p.status = 'PENDING'")
    Optional<PhaseApprovalRequest> findPendingByProcessAndPhase(
        @Param("idProcess") UUID idProcess, 
        @Param("idPhase") String idPhase
    );

    /**
     * Verificar si existe una solicitud pendiente para un proceso y fase
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PhaseApprovalRequest p WHERE p.process.idProcess = :idProcess AND p.idPhase = :idPhase AND p.status = 'PENDING'")
    boolean existsPendingByProcessAndPhase(@Param("idProcess") UUID idProcess, @Param("idPhase") String idPhase);

    /**
     * Buscar la última solicitud aprobada de un proceso
     */
    @Query("SELECT p FROM PhaseApprovalRequest p WHERE p.process.idProcess = :idProcess AND p.status = 'APPROVED' ORDER BY p.phaseOrder DESC")
    List<PhaseApprovalRequest> findApprovedByProcess(@Param("idProcess") UUID idProcess);

    /**
     * Contar solicitudes pendientes
     */
    @Query("SELECT COUNT(p) FROM PhaseApprovalRequest p WHERE p.status = 'PENDING'")
    long countPending();

    /**
     * Buscar historial de aprobaciones de un proceso
     */
    @Query("SELECT p FROM PhaseApprovalRequest p WHERE p.process.idProcess = :idProcess ORDER BY p.phaseOrder ASC, p.requestedAt DESC")
    List<PhaseApprovalRequest> findHistoryByProcess(@Param("idProcess") UUID idProcess);

    /**
     * Buscar solicitudes por usuario que solicitó
     */
    List<PhaseApprovalRequest> findByRequestedByOrderByRequestedAtDesc(String requestedBy);

    /**
     * Buscar la última solicitud (aprobada o rechazada) de una fase específica
     */
    @Query("SELECT p FROM PhaseApprovalRequest p WHERE p.process.idProcess = :idProcess AND p.idPhase = :idPhase ORDER BY p.requestedAt DESC")
    List<PhaseApprovalRequest> findByProcessAndPhase(@Param("idProcess") UUID idProcess, @Param("idPhase") String idPhase);
}
