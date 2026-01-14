package com.espe.ListoEgsi.controller.phase;

import com.espe.ListoEgsi.domain.dto.phase.EgsiAnswerDTO;
import com.espe.ListoEgsi.domain.dto.phase.SaveAnswersRequestDTO;
import com.espe.ListoEgsi.service.phase.EgsiAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de respuestas de fases EGSI.
 */
@Slf4j
@RestController
@RequestMapping("/api/v3/egsi/answers")
@RequiredArgsConstructor
@Tag(name = "Respuestas EGSI", description = "API para gestión de respuestas a preguntas EGSI")
@SecurityRequirement(name = "bearerAuth")
public class EgsiAnswerController {

    private final EgsiAnswerService answerService;

    @PostMapping
    @Operation(summary = "Guardar respuestas", description = "Guarda o actualiza múltiples respuestas para una fase")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuestas guardadas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> saveAnswers(
            @Valid @RequestBody SaveAnswersRequestDTO request,
            Authentication authentication) {
        try {
            String username = authentication != null ? authentication.getName() : "anonymous";
            List<EgsiAnswerDTO> savedAnswers = answerService.saveAnswers(request, username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Respuestas guardadas exitosamente");
            response.put("savedCount", savedAnswers.size());
            response.put("answers", savedAnswers);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al guardar respuestas: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/process/{idProcess}")
    @Operation(summary = "Obtener respuestas por proceso", description = "Recupera todas las respuestas de un proceso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuestas encontradas"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<EgsiAnswerDTO>> getAnswersByProcess(@PathVariable String idProcess) {
        List<EgsiAnswerDTO> answers = answerService.getAnswersByProcess(idProcess);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/process/{idProcess}/phase/{idPhase}")
    @Operation(summary = "Obtener respuestas por proceso y fase", description = "Recupera las respuestas de una fase específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuestas encontradas"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<EgsiAnswerDTO>> getAnswersByProcessAndPhase(
            @PathVariable String idProcess,
            @PathVariable String idPhase) {
        List<EgsiAnswerDTO> answers = answerService.getAnswersByProcessAndPhase(idProcess, idPhase);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/process/{idProcess}/phase/{idPhase}/map")
    @Operation(summary = "Obtener respuestas como mapa", description = "Recupera las respuestas como un mapa idQuestion -> answerValue")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuestas encontradas"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Map<String, String>> getAnswersMap(
            @PathVariable String idProcess,
            @PathVariable String idPhase) {
        Map<String, String> answersMap = answerService.getAnswersMapByProcessAndPhase(idProcess, idPhase);
        return ResponseEntity.ok(answersMap);
    }

    @GetMapping("/process/{idProcess}/phase/{idPhase}/progress")
    @Operation(summary = "Obtener progreso de fase", description = "Obtiene el porcentaje de progreso de una fase")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Progreso calculado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Map<String, Object>> getPhaseProgress(
            @PathVariable String idProcess,
            @PathVariable String idPhase) {
        int progress = answerService.getPhaseProgress(idProcess, idPhase);
        Map<String, Object> response = new HashMap<>();
        response.put("idProcess", idProcess);
        response.put("idPhase", idPhase);
        response.put("progress", progress);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idAnswer}")
    @Operation(summary = "Eliminar respuesta", description = "Elimina una respuesta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuesta eliminada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Map<String, String>> deleteAnswer(@PathVariable String idAnswer) {
        answerService.deleteAnswer(idAnswer);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Respuesta eliminada exitosamente");
        return ResponseEntity.ok(response);
    }
}
