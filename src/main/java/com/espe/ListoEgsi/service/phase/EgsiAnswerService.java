package com.espe.ListoEgsi.service.phase;

import com.espe.ListoEgsi.domain.dto.phase.EgsiAnswerDTO;
import com.espe.ListoEgsi.domain.dto.phase.SaveAnswersRequestDTO;

import java.util.List;
import java.util.Map;

/**
 * Servicio para gestión de respuestas EGSI.
 */
public interface EgsiAnswerService {

    /**
     * Guarda o actualiza múltiples respuestas para una fase.
     */
    List<EgsiAnswerDTO> saveAnswers(SaveAnswersRequestDTO request, String username);

    /**
     * Obtiene todas las respuestas de un proceso.
     */
    List<EgsiAnswerDTO> getAnswersByProcess(String idProcess);

    /**
     * Obtiene todas las respuestas de un proceso y fase.
     */
    List<EgsiAnswerDTO> getAnswersByProcessAndPhase(String idProcess, String idPhase);

    /**
     * Obtiene las respuestas como un mapa (idQuestion -> answerValue).
     */
    Map<String, String> getAnswersMapByProcessAndPhase(String idProcess, String idPhase);

    /**
     * Obtiene el progreso de una fase (porcentaje de preguntas respondidas).
     */
    int getPhaseProgress(String idProcess, String idPhase);

    /**
     * Elimina una respuesta específica.
     */
    void deleteAnswer(String idAnswer);

    /**
     * Elimina todas las respuestas de un proceso.
     */
    void deleteAnswersByProcess(String idProcess);
}
