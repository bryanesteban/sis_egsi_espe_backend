package com.espe.ListoEgsi.controller.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.espe.ListoEgsi.domain.dto.question.ResponsibleSigningDTO;
import com.espe.ListoEgsi.service.question.ResponsibleSigningService;

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
 * Controlador REST para gestión de responsables de firma de fases EGSI.
 * Administra los responsables que deben aprobar y firmar cada fase del proceso.
 * 
 * @version 1.0
 * @since 2026-01-10
 */
@Slf4j
@RestController
@RequestMapping("/api/responsible-signings")
@RequiredArgsConstructor
@Tag(name = "Responsables de Firma", description = "API para gestión de responsables de aprobación y firma de fases EGSI")
@SecurityRequirement(name = "bearerAuth")
public class ResponsibleSigningController {

    private final ResponsibleSigningService responsibleSigningService;

    @PostMapping
    @Operation(
        summary = "Crear un nuevo responsable de firma",
        description = "Asigna un nuevo responsable de aprobación y firma para una fase específica del proceso EGSI"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Responsable de firma creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> createResponsibleSigning(
            @Parameter(description = "Datos del nuevo responsable de firma", required = true)
            @Valid @RequestBody ResponsibleSigningDTO responsibleSigningDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in create responsible signing request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        ResponsibleSigningDTO createdResponsibleSigning = responsibleSigningService.createResponsibleSigning(responsibleSigningDTO);
        return new ResponseEntity<>(createdResponsibleSigning, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar un responsable de firma existente",
        description = "Modifica los datos de un responsable de firma incluyendo estado de aprobación y comentarios"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Responsable de firma actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Responsable de firma no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> updateResponsibleSigning(
            @Parameter(description = "UUID del responsable de firma a actualizar", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "Datos actualizados del responsable de firma", required = true)
            @Valid @RequestBody ResponsibleSigningDTO responsibleSigningDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in update responsible signing request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        ResponsibleSigningDTO updatedResponsibleSigning = responsibleSigningService.updateResponsibleSigning(id, responsibleSigningDTO);
        return ResponseEntity.ok(updatedResponsibleSigning);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar un responsable de firma",
        description = "Elimina permanentemente la asignación de un responsable de firma. Esta acción no se puede deshacer."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Responsable de firma eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Responsable de firma no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Void> deleteResponsibleSigning(
            @Parameter(description = "UUID del responsable de firma a eliminar", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        responsibleSigningService.deleteResponsibleSigning(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener un responsable de firma por ID",
        description = "Recupera los detalles completos de un responsable de firma específico por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Responsable de firma encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Responsable de firma no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<ResponsibleSigningDTO> getResponsibleSigningById(
            @Parameter(description = "UUID del responsable de firma", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        ResponsibleSigningDTO responsibleSigning = responsibleSigningService.getResponsibleSigningById(id);
        return ResponseEntity.ok(responsibleSigning);
    }

    @GetMapping
    @Operation(
        summary = "Obtener todos los responsables de firma",
        description = "Recupera una lista completa de todos los responsables de firma registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de responsables de firma recuperada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<ResponsibleSigningDTO>> getAllResponsibleSignings() {
        List<ResponsibleSigningDTO> responsibleSignings = responsibleSigningService.getAllResponsibleSignings();
        return ResponseEntity.ok(responsibleSignings);
    }

    @GetMapping("/phase/{idPhase}")
    @Operation(
        summary = "Obtener responsables de firma por fase",
        description = "Recupera todos los responsables asignados para aprobar y firmar una fase específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de responsables de firma de la fase recuperada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<ResponsibleSigningDTO>> getResponsibleSigningsByPhase(
            @Parameter(description = "UUID de la fase", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID idPhase) {
        List<ResponsibleSigningDTO> responsibleSignings = responsibleSigningService.getResponsibleSigningsByPhase(idPhase);
        return ResponseEntity.ok(responsibleSignings);
    }
}
