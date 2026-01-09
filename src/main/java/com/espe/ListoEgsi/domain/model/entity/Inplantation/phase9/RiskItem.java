package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase9;

import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.*;
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
@Table(name = "RISK_ITEM")
public class RiskItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_RISK", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idRisk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PLAN_TRATAMIENT", referencedColumnName = "ID_PLAN_TRATAMIENT")
    private RiskTreatmentPlan riskTreatmentPlan;

    @Size(max = 20)
    @Column(name = "RISK_ID_LITERAL")
    private String riskIdLiteral;

    @Size(max = 20)
    @Column(name = "TYPE_ASSET")
    private String typeAsset;

    @Size(max = 20)
    @Column(name = "RISK_LEVEL")
    private String riskLevel;

    @Size(max = 20)
    @Column(name = "TREATMENT_OPTION")
    private String treatmentOption;

    @Size(max = 500)
    @Column(name = "CONTROLS_FOR_IMPLEMENTATION")
    private String controlsForImplementation;

    @Size(max = 500)
    @Column(name = "DESCRIPTION_ACTIVITIES")
    private String descriptionActivities;

    @Size(max = 500)
    @Column(name = "IMPLEMENTATION_RESPONSABILITY")
    private String implementationResponsability;

    @Size(max = 50)
    @Column(name = "FUNTIONAL_AREA")
    private String funtionalArea;

    @Size(max = 20)
    @Column(name = "CML_STATE")
    private String cmlState;

    @Size(max = 20)
    @Column(name = "DATE_BEGIN")
    private String dateBegin;

    @Size(max = 20)
    @Column(name = "DATE_END")
    private String dateEnd;

    @Size(max = 20)
    @Column(name = "HOUR_DAY")
    private String hourDay;

    @Size(max = 20)
    @Column(name = "MONEY")
    private String money;
}