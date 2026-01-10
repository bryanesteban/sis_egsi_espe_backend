package com.espe.ListoEgsi.domain.model.entity.question;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;

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
@Table(name = "PHASE_CUSTOM")
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PHASE", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idPhase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false)
    private ProcessEgsi process;

    @NotBlank
    @Size(max = 50)
    @Column(name = "QUESTIONARY_CODE", nullable = false)
    private String questionaryCode;

    @NotBlank
    @Size(max = 500)
    @Column(name = "RESPONSIBLES", nullable = false)
    private String responsibles;

    @NotBlank
    @Size(max = 50)
    @Column(name = "STATUS", nullable = false)
    private String status;

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResponsibleSigning> responsibleSignings;
}
