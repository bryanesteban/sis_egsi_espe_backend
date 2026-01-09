package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
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
@Table(name = "PROCESS")
public class ProcessEgsi {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PROCESS", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idProcess;

    @NotBlank
    @Size(max = 200)
    @Column(name = "RELATED_PROGRAM", nullable = false)
    private String relatedProgram;

    @NotBlank
    @Size(max = 200)
    @Column(name = "PROJECT_LEADER", nullable = false)
    private String projectLeader;

    @NotBlank
    @Size(max = 20)
    @Column(name = "DATE_BEGIN", nullable = false)
    private String dateBegin;

    @NotBlank
    @Size(max = 20)
    @Column(name = "DATE_END", nullable = false)
    private String dateEnd;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "PROBLEM", nullable = false)
    private String problem;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "JUSTIFICATION", nullable = false)
    private String justification;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "PROJECT_DESCRIPTION", nullable = false)
    private String projectDescription;

    @NotBlank
    @Size(max = 500)
    @Column(name = "BENEFITS", nullable = false)
    private String benefits;

    @NotBlank
    @Size(max = 500)
    @Column(name = "GENERAL_OBJECTIVE", nullable = false)
    private String generalObjective;

    @NotBlank
    @Size(max = 500)
    @Column(name = "BENEFICIARIES", nullable = false)
    private String beneficiaries;

    @NotBlank
    @Size(max = 200)
    @Column(name = "IMPLETENTATION_PERIOD", nullable = false)
    private String implementationPeriod;
}