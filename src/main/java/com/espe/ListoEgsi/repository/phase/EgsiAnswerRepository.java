package com.espe.ListoEgsi.repository.phase;

import com.espe.ListoEgsi.domain.model.entity.phase.EgsiAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para operaciones CRUD de respuestas EGSI.
 */
@Repository
public interface EgsiAnswerRepository extends JpaRepository<EgsiAnswer, String> {

    /**
     * Busca todas las respuestas de un proceso específico.
     */
    List<EgsiAnswer> findByProcessIdProcess(UUID idProcess);

    /**
     * Busca todas las respuestas de un proceso y fase específica.
     */
    @Query("SELECT a FROM EgsiAnswer a WHERE a.process.idProcess = :idProcess AND a.idPhase = :idPhase")
    List<EgsiAnswer> findByProcessAndPhase(@Param("idProcess") UUID idProcess, @Param("idPhase") String idPhase);

    /**
     * Busca una respuesta específica por proceso y pregunta.
     */
    @Query("SELECT a FROM EgsiAnswer a WHERE a.process.idProcess = :idProcess AND a.question.idQuestion = :idQuestion")
    Optional<EgsiAnswer> findByProcessAndQuestion(@Param("idProcess") UUID idProcess, @Param("idQuestion") String idQuestion);

    /**
     * Cuenta las respuestas completadas de una fase específica.
     */
    @Query("SELECT COUNT(a) FROM EgsiAnswer a WHERE a.process.idProcess = :idProcess AND a.idPhase = :idPhase AND a.status = 'COMPLETED'")
    long countCompletedByProcessAndPhase(@Param("idProcess") UUID idProcess, @Param("idPhase") String idPhase);

    /**
     * Cuenta todas las respuestas de una fase específica.
     */
    @Query("SELECT COUNT(a) FROM EgsiAnswer a WHERE a.process.idProcess = :idProcess AND a.idPhase = :idPhase")
    long countByProcessAndPhase(@Param("idProcess") UUID idProcess, @Param("idPhase") String idPhase);

    /**
     * Elimina todas las respuestas de un proceso.
     */
    void deleteByProcessIdProcess(UUID idProcess);
}
