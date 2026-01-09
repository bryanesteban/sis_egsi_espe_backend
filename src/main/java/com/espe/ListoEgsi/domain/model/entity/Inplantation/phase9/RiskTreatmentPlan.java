package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase9;

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
@Table(name = "RISK_TREATAMENT_PLAN")
public class RiskTreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PLAN_TRATAMIENT", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idPlanTratamient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false, referencedColumnName = "ID_PROCESS")
    private ProcessEgsi process;

    @Size(max = 100)
    @Column(name = "INSTITUTION")
    private String institution;

    @Size(max = 100)
    @Column(name = "CONFIDENTIALITY_LEVEL")
    private String confidentialityLevel;

    @Size(max = 100)
    @Column(name = "DATE_LAST_MODIFICATION")
    private String dateLastModification;

    @Size(max = 100)
    @Column(name = "DOCUMENT_RESPONSIBLE")
    private String documentResponsible;
}
