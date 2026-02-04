package com.espe.ListoEgsi.service.approval;

import com.espe.ListoEgsi.domain.dto.phase.CreateApprovalRequestDTO;
import com.espe.ListoEgsi.domain.dto.phase.PhaseApprovalDTO;
import com.espe.ListoEgsi.domain.dto.phase.ReviewApprovalRequestDTO;
import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.question.PhaseApprovalRequest;
import com.espe.ListoEgsi.exception.ResourceNotFoundException;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.phase.PhaseApprovalRepository;
import com.espe.ListoEgsi.service.phase.impl.PhaseApprovalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ApprovalService
 * Cubre TC-UT-015 a TC-UT-018
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ApprovalService - Tests Unitarios")
class ApprovalServiceTest {

    @Mock
    private PhaseApprovalRepository approvalRepository;

    @Mock
    private ProcessRepository processRepository;

    @InjectMocks
    private PhaseApprovalServiceImpl approvalService;

    private CreateApprovalRequestDTO validRequest;
    private ProcessEgsi validProcess;
    private PhaseApprovalRequest validApproval;
    private ReviewApprovalRequestDTO reviewRequest;

    @BeforeEach
    void setUp() {
        // Preparar proceso válido
        validProcess = ProcessEgsi.builder()
                .idProcess(UUID.randomUUID())
                .name("Proceso Test")
                .customPhase("FASE1")
                .build();

        // Preparar request válido
        validRequest = CreateApprovalRequestDTO.builder()
                .idProcess(validProcess.getIdProcess())
                .idPhase("PHASE001")
                .phaseOrder(1)
                .phaseTitle("Fase 1 - Preparación")
                .requestedBy("testuser")
                .comments("Solicitud de prueba")
                .build();

        // Preparar aprobación válida
        validApproval = PhaseApprovalRequest.builder()
                .idApproval(UUID.randomUUID())
                .process(validProcess)
                .idPhase("PHASE001")
                .phaseOrder(1)
                .phaseTitle("Fase 1 - Preparación")
                .status("PENDING")
                .requestedBy("testuser")
                .requestedAt(LocalDateTime.now())
                .build();

        // Preparar review request
        reviewRequest = ReviewApprovalRequestDTO.builder()
                .idApproval(validApproval.getIdApproval())
                .action("APPROVED")
                .reviewedBy("admin")
                .build();
    }

    /**
     * TC-UT-015: Validar aprobación con datos válidos
     * 
     * Verifica que:
     * - Se crea la solicitud correctamente
     * - Sin errores
     */
    @Test
    @DisplayName("TC-UT-015: Aprobación se crea correctamente con datos válidos")
    void testValidApproval() {
        // Arrange
        when(approvalRepository.existsPendingByProcessAndPhase(
                validRequest.getIdProcess(), 
                validRequest.getIdPhase()
        )).thenReturn(false);
        
        when(processRepository.findById(validRequest.getIdProcess()))
                .thenReturn(Optional.of(validProcess));
        
        when(approvalRepository.save(any(PhaseApprovalRequest.class)))
                .thenReturn(validApproval);

        // Act
        PhaseApprovalDTO result = approvalService.createApprovalRequest(validRequest);

        // Assert
        assertNotNull(result, "El resultado no debe ser null");
        assertEquals(validApproval.getIdApproval(), result.getIdApproval());
        assertEquals("PENDING", result.getStatus());
        assertEquals("testuser", result.getRequestedBy());

        // Verificar que se llamaron los métodos correctos
        verify(approvalRepository, times(1)).existsPendingByProcessAndPhase(any(UUID.class), anyString());
        verify(processRepository, times(1)).findById(any(UUID.class));
        verify(approvalRepository, times(1)).save(any(PhaseApprovalRequest.class));
    }

    /**
     * TC-UT-016: Validar aprobación requiere processId
     * 
     * Verifica que:
     * - Validación falla sin processId
     * - Excepción ResourceNotFoundException
     * - Mensaje: "Proceso no encontrado"
     */
    @Test
    @DisplayName("TC-UT-016: Aprobación requiere processId")
    void testApprovalRequiresProcessId() {
        // Arrange - ProcessId que no existe
        UUID nonExistentProcessId = UUID.randomUUID();
        CreateApprovalRequestDTO invalidRequest = CreateApprovalRequestDTO.builder()
                .idProcess(nonExistentProcessId)
                .idPhase("PHASE001")
                .phaseOrder(1)
                .phaseTitle("Fase 1 - Preparación")
                .requestedBy("testuser")
                .build();

        when(approvalRepository.existsPendingByProcessAndPhase(
                invalidRequest.getIdProcess(), 
                invalidRequest.getIdPhase()
        )).thenReturn(false);
        
        when(processRepository.findById(nonExistentProcessId))
                .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            approvalService.createApprovalRequest(invalidRequest);
        }, "Debe lanzar excepción cuando el proceso no existe");

        assertTrue(exception.getMessage().contains("Proceso no encontrado"),
                "El mensaje debe indicar que el proceso no se encontró");

        // Verificar que NO se guardó ninguna aprobación
        verify(approvalRepository, never()).save(any(PhaseApprovalRequest.class));
    }

    /**
     * TC-UT-017: Validar aprobación requiere phaseId
     * 
     * Verifica que:
     * - Validación falla sin phaseId
     * - El sistema requiere phaseId explícito
     */
    @Test
    @DisplayName("TC-UT-017: Aprobación requiere phaseId")
    void testApprovalRequiresPhaseId() {
        // Arrange - Request sin phaseId (null o vacío)
        CreateApprovalRequestDTO invalidRequest = CreateApprovalRequestDTO.builder()
                .idProcess(validProcess.getIdProcess())
                .idPhase(null)  // Sin phaseId
                .phaseOrder(1)
                .phaseTitle("Fase 1 - Preparación")
                .requestedBy("testuser")
                .build();

        // No se necesita mockear processRepository porque la validación falla antes

        // Act & Assert
        // La validación en el servicio debe prevenir este caso
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            approvalService.createApprovalRequest(invalidRequest);
        }, "Debe lanzar excepción cuando phaseId es null");

        // Verificar mensaje de error
        assertTrue(exception.getMessage().contains("fase") || 
                   exception.getMessage().contains("requerido"),
                "El mensaje debe indicar que la fase es requerida");

        // Verificar que NO se guardó ninguna aprobación
        verify(approvalRepository, never()).save(any(PhaseApprovalRequest.class));
    }

    /**
     * TC-UT-018: Validar aprobación requiere comentario en rechazo
     * 
     * Verifica que:
     * - Validación falla si action=REJECTED sin rejectionReason
     * - Excepción IllegalArgumentException
     * - Mensaje: "Debe proporcionar una razón de rechazo"
     */
    @Test
    @DisplayName("TC-UT-018: Aprobación rechazada requiere comentario")
    void testApprovalRequiresCommentOnRejection() {
        // Arrange - Review con REJECTED pero sin rejectionReason
        ReviewApprovalRequestDTO rejectWithoutReason = ReviewApprovalRequestDTO.builder()
                .idApproval(validApproval.getIdApproval())
                .action("REJECTED")
                .reviewedBy("admin")
                .rejectionReason(null)  // Sin razón de rechazo
                .build();

        when(approvalRepository.findById(validApproval.getIdApproval()))
                .thenReturn(Optional.of(validApproval));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            approvalService.reviewApprovalRequest(rejectWithoutReason);
        }, "Debe lanzar excepción cuando se rechaza sin proporcionar razón");

        assertTrue(exception.getMessage().contains("razón de rechazo") || 
                   exception.getMessage().contains("Debe proporcionar"),
                "El mensaje debe indicar que se requiere una razón de rechazo");

        // Verificar que NO se guardó el cambio
        verify(approvalRepository, never()).save(any(PhaseApprovalRequest.class));
    }

    /**
     * Test adicional: Validar aprobación exitosa (APPROVED)
     */
    @Test
    @DisplayName("Aprobar solicitud funciona correctamente")
    void testApproveRequest() {
        // Arrange
        PhaseApprovalRequest approvedRequest = PhaseApprovalRequest.builder()
                .idApproval(validApproval.getIdApproval())
                .process(validProcess)
                .idPhase("PHASE001")
                .phaseOrder(1)
                .phaseTitle("Fase 1 - Preparación")
                .status("APPROVED")  // Estado aprobado
                .requestedBy("testuser")
                .requestedAt(LocalDateTime.now())
                .reviewedBy("admin")
                .reviewedAt(LocalDateTime.now())
                .build();

        when(approvalRepository.findById(reviewRequest.getIdApproval()))
                .thenReturn(Optional.of(validApproval));
        when(approvalRepository.save(any(PhaseApprovalRequest.class)))
                .thenReturn(approvedRequest);

        // Act
        PhaseApprovalDTO result = approvalService.reviewApprovalRequest(reviewRequest);

        // Assert
        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());
        assertEquals("admin", result.getReviewedBy());
        assertNotNull(result.getReviewedAt());

        verify(approvalRepository, times(1)).findById(validApproval.getIdApproval());
        verify(approvalRepository, times(1)).save(any(PhaseApprovalRequest.class));
    }

    /**
     * Test adicional: Validar rechazo con razón válida
     */
    @Test
    @DisplayName("Rechazar solicitud con razón válida funciona correctamente")
    void testRejectRequestWithValidReason() {
        // Arrange
        ReviewApprovalRequestDTO rejectWithReason = ReviewApprovalRequestDTO.builder()
                .idApproval(validApproval.getIdApproval())
                .action("REJECTED")
                .reviewedBy("admin")
                .rejectionReason("Falta información en la fase")
                .build();

        PhaseApprovalRequest rejectedRequest = PhaseApprovalRequest.builder()
                .idApproval(validApproval.getIdApproval())
                .process(validProcess)
                .idPhase("PHASE001")
                .phaseOrder(1)
                .phaseTitle("Fase 1 - Preparación")
                .status("REJECTED")
                .requestedBy("testuser")
                .requestedAt(LocalDateTime.now())
                .reviewedBy("admin")
                .reviewedAt(LocalDateTime.now())
                .rejectionReason("Falta información en la fase")
                .build();

        when(approvalRepository.findById(validApproval.getIdApproval()))
                .thenReturn(Optional.of(validApproval));
        when(approvalRepository.save(any(PhaseApprovalRequest.class)))
                .thenReturn(rejectedRequest);

        // Act
        PhaseApprovalDTO result = approvalService.reviewApprovalRequest(rejectWithReason);

        // Assert
        assertNotNull(result);
        assertEquals("REJECTED", result.getStatus());
        assertEquals("admin", result.getReviewedBy());
        assertEquals("Falta información en la fase", result.getRejectionReason());
        assertNotNull(result.getReviewedAt());

        verify(approvalRepository, times(1)).save(any(PhaseApprovalRequest.class));
    }

    /**
     * Test adicional: Validar que no se puede revisar solicitud ya procesada
     */
    @Test
    @DisplayName("No se puede revisar una solicitud ya procesada")
    void testCannotReviewProcessedRequest() {
        // Arrange - Solicitud ya aprobada
        PhaseApprovalRequest processedRequest = PhaseApprovalRequest.builder()
                .idApproval(validApproval.getIdApproval())
                .process(validProcess)
                .idPhase("PHASE001")
                .phaseOrder(1)
                .phaseTitle("Fase 1 - Preparación")
                .status("APPROVED")  // Ya procesada
                .requestedBy("testuser")
                .requestedAt(LocalDateTime.now())
                .reviewedBy("admin")
                .reviewedAt(LocalDateTime.now())
                .build();

        when(approvalRepository.findById(reviewRequest.getIdApproval()))
                .thenReturn(Optional.of(processedRequest));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            approvalService.reviewApprovalRequest(reviewRequest);
        }, "Debe lanzar excepción cuando se intenta revisar una solicitud ya procesada");

        assertTrue(exception.getMessage().contains("pendientes"),
                "El mensaje debe indicar que solo se pueden revisar solicitudes pendientes");

        verify(approvalRepository, never()).save(any(PhaseApprovalRequest.class));
    }

    /**
     * Test adicional: Validar solicitud duplicada
     */
    @Test
    @DisplayName("No se permite solicitud duplicada para mismo proceso y fase")
    void testCannotCreateDuplicateRequest() {
        // Arrange - Ya existe solicitud pendiente
        when(approvalRepository.existsPendingByProcessAndPhase(
                validRequest.getIdProcess(), 
                validRequest.getIdPhase()
        )).thenReturn(true);  // Ya existe

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            approvalService.createApprovalRequest(validRequest);
        }, "Debe lanzar excepción cuando ya existe solicitud pendiente");

        assertTrue(exception.getMessage().contains("Ya existe"),
                "El mensaje debe indicar que ya existe una solicitud pendiente");

        // Verificar que NO se creó nueva solicitud
        verify(approvalRepository, never()).save(any(PhaseApprovalRequest.class));
    }
}
