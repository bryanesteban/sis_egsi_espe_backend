package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase4;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PLAN_FOR_INTERNAL_EVALUATION")
public class PlanInternalEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PLAN_INTERAL_EVAL",  nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idPlanInternalEval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false, referencedColumnName = "ID_PROCESS")
    private ProcessEgsi process;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "BACKGROUND", nullable = false)
    private String background;

    @NotBlank
    @Size(max = 500)
    @Column(name = "DOCUMENT_OBJETIVE", nullable = false)
    private String documentObjetive;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "SCOPE_PLAN", nullable = false)
    private String scopePlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "CRITERION_PLAN", nullable = false)
    private String criterionPlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "METHODOLOGY_PLAN", nullable = false)
    private String methodologyPlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "CRONOGRAM_PLAN", nullable = false)
    private String cronogramPlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "RESOURCE_PLAN", nullable = false)
    private String resourcePlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "DOCUMENTATION_FINDINGS_PLAN", nullable = false)
    private String documentationFindingsPlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "DOCUMENTATION_FINDINGS_PLAN_TABLE", nullable = false)
    private String documentationFindingsPlanTable;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "RESULTS_PLAN", nullable = false)
    private String resultsPlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "GLOSARY_TERMS_TABLE", nullable = false)
    private String glosaryTermsTable;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "DOCUMENTS_REFERENCE", nullable = false)
    private String documentsReference;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "CONTROL_VERSION", nullable = false)
    private String controlVersion;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "CHANGE_HISTORIAL", nullable = false)
    private String changeHistorial;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "SCHEDULE_WORKING", nullable = false)
    private String scheduleWorking;
    

}
