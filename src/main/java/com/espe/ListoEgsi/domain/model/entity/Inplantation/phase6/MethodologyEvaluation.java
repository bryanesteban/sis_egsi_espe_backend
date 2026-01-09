package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase6;

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
@Table(name = "METHODOLOGY_EVALUATION")
public class MethodologyEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_METHODOLOGY_EVAL", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idMethodologyEval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false, referencedColumnName = "ID_PROCESS")
    private ProcessEgsi process;

    @Size(max = 500)
    @Column(name = "OBJETIVE")
    private String objetive;

    @Size(max = 1000)
    @Column(name = "SCOPE")
    private String scope;

    @Size(max = 500)
    @Column(name = "USERS")
    private String users;

    @Size(max = 1000)
    @Column(name = "PROCESS_METHODOLOGY")
    private String processMethodology;

    @Size(max = 1000)
    @Column(name = "ASSETS_METHODOLOGY")
    private String assetsMethodology;

    @Size(max = 1000)
    @Column(name = "IDENTIFICATION_METHODOLOGY")
    private String identificationMethodology;

    @Size(max = 1000)
    @Column(name = "IMPACT_PROBABILITY")
    private String impactProbability;

    @Size(max = 1000)
    @Column(name = "ACCEPTANCE_CRITERIA_METHODOLOGY")
    private String acceptanceCriteriaMethodology;

    @Size(max = 1000)
    @Column(name = "RISK_TREATMENT_METHODOLOGY")
    private String riskTreatmentMethodology;

    @Size(max = 1000)
    @Column(name = "REVISION_PERIOD_METHODOLOGY")
    private String revisionPeriodMethodology;

    @Size(max = 1000)
    @Column(name = "DECLARATION_APPLYCABILITY_METHODOLOGY")
    private String declarationApplycabilityMethodology;

    @Size(max = 1000)
    @Column(name = "REPORTS_METHODOLOGY")
    private String reportsMethodology;

    @Size(max = 1000)
    @Column(name = "VALIDITY_MANAGEMENT_METHODOLOGY")
    private String validityManagementMethodology;

    @Size(max = 1000)
    @Column(name = "NOTES_METHODOLOGY")
    private String notesMethodology;
}
