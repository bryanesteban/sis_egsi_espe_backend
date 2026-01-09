package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase3;

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
@Table(name = "PLAN_OF_COMUNICATION")
public class PlanOfComunication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PLAN_COMU", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idPlanComunication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false, referencedColumnName = "ID_PROCESS")
    private ProcessEgsi process;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "BACKGROUND", nullable = false)
    private String background;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "OBJETIVE_OF_PLAN", nullable = false)
    private String objetiveOfPlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "SPECIFIC_OBJETIVES", nullable = false)
    private String specificObjetives;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "PLAN_DESCRIPTION", nullable = false)
    private String planDescription;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "SCOPE_PLAN", nullable = false)
    private String scopePlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "DESING_PLAN", nullable = false)
    private String desingPlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "MATERIALS_AND_CHANNELS", nullable = false)
    private String materialsAndChannels;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "STRATEGIES_PLAN", nullable = false)
    private String strategiesPlan;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "NEEDS_IDENTIFICATIONS", nullable = false)
    private String needsIdentifications;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "ROLES_AND_RESPONSABILITIES", nullable = false)
    private String rolesAndResponsabilities;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "RESULTS_AND_ACHIEVEMENTS", nullable = false)
    private String resultsAndAchievements;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "SCHEDULE_ACTIVITIES", nullable = false)
    private String scheduleActivities;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "GLOSARY_TERMS", nullable = false)
    private String glosaryTerms;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "DOCUMENTS_OF_REFERENCE", nullable = false)
    private String documentsOfReference;

    @NotBlank
    @Size(max = 20)
    @Column(name = "STATE", nullable = false)
    private String state;

}
