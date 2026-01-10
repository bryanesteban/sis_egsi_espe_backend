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
    @Size(max = 50)
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @NotBlank
    @Size(max = 20)
    @Column(name = "DATE_BEGIN", nullable = false)
    private String dateBegin;

    @NotBlank
    @Size(max = 20)
    @Column(name = "DATE_END", nullable = false)
    private String dateEnd;

    @NotBlank
    @Size(max = 20)
    @Column(name = "ACTIVE", nullable = false)
    private String active;

    @Size(max = 10)
    @Column(name = "CURRENT_PHASE")
    private String currentPhase;
}