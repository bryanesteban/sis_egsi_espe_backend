package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase7;

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
@Table(name = "REPORT_EVALUATION_AND_TREATMENT_RISK")
public class ReportEvaluationOfTreatmentRisk {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_REPORT_TREATMENT", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idReportTreatment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false, referencedColumnName = "ID_PROCESS")
    private ProcessEgsi process;

    @Size(max = 500)
    @Column(name = "OBJETIVE")
    private String objetive;

    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;

    @Size(max = 1000)
    @Column(name = "CONTEXT_EVALUATION")
    private String contextEvaluation;

    @Size(max = 1000)
    @Column(name = "METHODOLOGY_EVALUATION")
    private String methodologyEvaluation;

    @Size(max = 1000)
    @Column(name = "IDENTIFICATION_VALORATION_ASSETS")
    private String identificationValorationAssets;

    @Size(max = 1000)
    @Column(name = "IDENTIFICATION_RISK_AND_VULNERABILITIES")
    private String identificationRiskAndVulnerabilities;

    @Size(max = 1000)
    @Column(name = "IDENTIFICATION_CONTROL")
    private String identificationControl;

    @Size(max = 1000)
    @Column(name = "EVALUATION_RISK")
    private String evaluationRisk;

    @Size(max = 1000)
    @Column(name = "TREATMENT_RISK")
    private String treatmentRisk;

    @Size(max = 1000)
    @Column(name = "NIVEL_RISK")
    private String nivelRisk;

    @Size(max = 1000)
    @Column(name = "CLARIFICATION_NOTE")
    private String clarificationNote;
}