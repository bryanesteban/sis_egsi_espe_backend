package com.espe.ListoEgsi.controller.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.espe.ListoEgsi.domain.dto.question.QuestionDTO;
import com.espe.ListoEgsi.service.question.QuestionService;

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
 * Controlador REST para gestión de preguntas de cuestionarios EGSI.
 * Administra las preguntas que conforman los cuestionarios de evaluación.
 * 
 * @version 1.0
 * @since 2026-01-10
 */
@Slf4j
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Tag(name = "Preguntas", description = "API para gestión de preguntas de cuestionarios de evaluación EGSI")
@SecurityRequirement(name = "bearerAuth")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    @Operation(
        summary = "Crear una nueva pregunta",
        description = "Registra una nueva pregunta para un cuestionario EGSI con tipo, texto y opciones de respuesta"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pregunta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> createQuestion(
            @Parameter(description = "Datos de la nueva pregunta", required = true)
            @Valid @RequestBody QuestionDTO questionDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in create question request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        QuestionDTO createdQuestion = questionService.createQuestion(questionDTO);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar una pregunta existente",
        description = "Modifica el texto, tipo o configuración de una pregunta existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pregunta actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Pregunta no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> updateQuestion(
            @Parameter(description = "UUID de la pregunta a actualizar", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "Datos actualizados de la pregunta", required = true)
            @Valid @RequestBody QuestionDTO questionDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

            log.warn("Validation errors in update question request: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        QuestionDTO updatedQuestion = questionService.updateQuestion(id, questionDTO);
        return ResponseEntity.ok(updatedQuestion);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar una pregunta",
        description = "Elimina permanentemente una pregunta del sistema. Esta acción no se puede deshacer."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pregunta eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pregunta no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Void> deleteQuestion(
            @Parameter(description = "UUID de la pregunta a eliminar", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener una pregunta por ID",
        description = "Recupera los detalles completos de una pregunta específica por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pregunta encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pregunta no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<QuestionDTO> getQuestionById(
            @Parameter(description = "UUID de la pregunta", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        QuestionDTO question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    @GetMapping
    @Operation(
        summary = "Obtener todas las preguntas",
        description = "Recupera una lista completa de todas las preguntas registradas en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de preguntas recuperada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        List<QuestionDTO> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/questionary/{idQuestionary}")
    @Operation(
        summary = "Obtener preguntas por cuestionario",
        description = "Recupera todas las preguntas asociadas a un cuestionario específico ordenadas por secuencia"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de preguntas del cuestionario recuperada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cuestionario no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<QuestionDTO>> getQuestionsByQuestionary(
            @Parameter(description = "ID del cuestionario", required = true, example = "CUEST001")
            @PathVariable String idQuestionary) {
        List<QuestionDTO> questions = questionService.getQuestionsByQuestionary(idQuestionary);
        return ResponseEntity.ok(questions);
    }
}
