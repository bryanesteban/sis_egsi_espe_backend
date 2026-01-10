package com.espe.ListoEgsi.controller.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.espe.ListoEgsi.domain.dto.question.QuestionaryDTO;
import com.espe.ListoEgsi.service.question.QuestionaryService;

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
 * Controlador REST para gestión de cuestionarios EGSI.
 * Administra plantillas de cuestionarios que contienen preguntas de evaluación.
 * 
 * @version 1.0
 * @since 2026-01-10
 */
@Slf4j
@RestController
@RequestMapping("/api/questionaries")
@RequiredArgsConstructor
@Tag(name = "Cuestionarios", description = "API para gestión de plantillas de cuestionarios de evaluación EGSI")
@SecurityRequirement(name = "bearerAuth")
public class QuestionaryController {

    private final QuestionaryService questionaryService;

    @PostMapping
    @Operation(
        summary = "Crear un nuevo cuestionario",
        description = "Registra una nueva plantilla de cuestionario EGSI con nombre, tipo y descripción"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cuestionario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> createQuestionary(
            @Parameter(description = "Datos del nuevo cuestionario", required = true)
            @Valid @RequestBody QuestionaryDTO questionaryDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in create questionary request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        QuestionaryDTO createdQuestionary = questionaryService.createQuestionary(questionaryDTO);
        return new ResponseEntity<>(createdQuestionary, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar un cuestionario existente",
        description = "Modifica el nombre, tipo o descripción de un cuestionario existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuestionario actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Cuestionario no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> updateQuestionary(
            @Parameter(description = "ID del cuestionario a actualizar", required = true, example = "CUEST001")
            @PathVariable String id,
            @Parameter(description = "Datos actualizados del cuestionario", required = true)
            @Valid @RequestBody QuestionaryDTO questionaryDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in update questionary request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        QuestionaryDTO updatedQuestionary = questionaryService.updateQuestionary(id, questionaryDTO);
        return ResponseEntity.ok(updatedQuestionary);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar un cuestionario",
        description = "Elimina permanentemente un cuestionario del sistema. Esta acción no se puede deshacer."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cuestionario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cuestionario no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Void> deleteQuestionary(
            @Parameter(description = "ID del cuestionario a eliminar", required = true, example = "CUEST001")
            @PathVariable String id) {
        questionaryService.deleteQuestionary(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener un cuestionario por ID",
        description = "Recupera los detalles completos de un cuestionario específico por su identificador"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuestionario encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cuestionario no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<QuestionaryDTO> getQuestionaryById(
            @Parameter(description = "ID del cuestionario", required = true, example = "CUEST001")
            @PathVariable String id) {
        QuestionaryDTO questionary = questionaryService.getQuestionaryById(id);
        return ResponseEntity.ok(questionary);
    }

    @GetMapping
    @Operation(
        summary = "Obtener todos los cuestionarios",
        description = "Recupera una lista completa de todas las plantillas de cuestionarios registradas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cuestionarios recuperada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<QuestionaryDTO>> getAllQuestionaries() {
        List<QuestionaryDTO> questionaries = questionaryService.getAllQuestionaries();
        return ResponseEntity.ok(questionaries);
    }

    @GetMapping("/phase/{phase}")
    @Operation(
        summary = "Obtener cuestionarios por fase",
        description = "Recupera todos los cuestionarios asociados a una fase específica del proceso EGSI"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cuestionarios de la fase recuperada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<QuestionaryDTO>> getQuestionariesByPhase(
            @Parameter(description = "Nombre de la fase", required = true, example = "Planificación")
            @PathVariable String phase) {
        List<QuestionaryDTO> questionaries = questionaryService.getQuestionariesByPhase(phase);
        return ResponseEntity.ok(questionaries);
    }
}
