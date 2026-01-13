package com.espe.ListoEgsi.controller.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            @Parameter(description = "ID de la pregunta a buscar", required = true, example = "1")
            @PathVariable Integer id) {
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
