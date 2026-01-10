package com.espe.ListoEgsi.controller.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.espe.ListoEgsi.domain.dto.question.AnswerDTO;
import com.espe.ListoEgsi.service.question.AnswerService;

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
 * Controlador REST para gestión de respuestas a cuestionarios EGSI.
 * Proporciona endpoints CRUD completos para administrar respuestas de evaluación.
 * 
 * @version 1.0
 * @since 2026-01-10
 */
@Slf4j
@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
@Tag(name = "Respuestas", description = "API para gestión de respuestas a cuestionarios de evaluación EGSI")
@SecurityRequirement(name = "bearerAuth")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    @Operation(
        summary = "Crear nueva respuesta",
        description = "Registra una nueva respuesta a una pregunta de cuestionario en una fase específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Respuesta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> createAnswer(
            @Parameter(description = "Datos de la nueva respuesta", required = true)
            @Valid @RequestBody AnswerDTO answerDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in create answer request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        AnswerDTO createdAnswer = answerService.createAnswer(answerDTO);
        return new ResponseEntity<>(createdAnswer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar una respuesta existente",
        description = "Modifica una respuesta previamente registrada incluyendo texto, archivos adjuntos y estado de aprobación"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuesta actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Respuesta no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> updateAnswer(
            @Parameter(description = "UUID de la respuesta a actualizar", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "Datos actualizados de la respuesta", required = true)
            @Valid @RequestBody AnswerDTO answerDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in update answer request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        AnswerDTO updatedAnswer = answerService.updateAnswer(id, answerDTO);
        return ResponseEntity.ok(updatedAnswer);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar una respuesta",
        description = "Elimina permanentemente una respuesta del sistema. Esta acción no se puede deshacer."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Respuesta eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Respuesta no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Void> deleteAnswer(
            @Parameter(description = "UUID de la respuesta a eliminar", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener una respuesta por ID",
        description = "Recupera los detalles completos de una respuesta específica por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuesta encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Respuesta no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<AnswerDTO> getAnswerById(
            @Parameter(description = "UUID de la respuesta", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        AnswerDTO answer = answerService.getAnswerById(id);
        return ResponseEntity.ok(answer);
    }

    @GetMapping
    @Operation(
        summary = "Obtener todas las respuestas",
        description = "Recupera una lista completa de todas las respuestas registradas en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de respuestas recuperada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<AnswerDTO>> getAllAnswers() {
        List<AnswerDTO> answers = answerService.getAllAnswers();
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/phase/{idPhase}")
    @Operation(
        summary = "Obtener respuestas por fase",
        description = "Recupera todas las respuestas asociadas a una fase específica del proceso"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de respuestas recuperada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Fase no encontrada")
    })
    public ResponseEntity<List<AnswerDTO>> getAnswersByPhase(
            @Parameter(description = "UUID de la fase", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID idPhase) {
        List<AnswerDTO> answers = answerService.getAnswersByPhase(idPhase);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/question/{idQuestion}")
    @Operation(
        summary = "Obtener respuestas por pregunta",
        description = "Recupera todas las respuestas asociadas a una pregunta específica del cuestionario"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de respuestas de la pregunta recuperada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pregunta no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<AnswerDTO>> getAnswersByQuestion(
            @Parameter(description = "UUID de la pregunta", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID idQuestion) {
        List<AnswerDTO> answers = answerService.getAnswersByQuestion(idQuestion);
        return ResponseEntity.ok(answers);
    }
}
