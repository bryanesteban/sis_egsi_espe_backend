package com.espe.ListoEgsi.domain.model.entity.question;

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
@Table(name = "RESPONSIBLES_SIGNING")
public class ResponsibleSigning {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_RESPONSIBLE", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idResponsible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PHASE", nullable = false)
    private Phase phase;

    @NotBlank
    @Size(max = 100)
    @Column(name = "NAME_RESPONSIBLE", nullable = false)
    private String nameResponsible;

    @NotBlank
    @Size(max = 50)
    @Column(name = "STATUS_SIGN", nullable = false)
    private String statusSign;

    @NotBlank
    @Size(max = 20)
    @Column(name = "CREATED_AT", nullable = false)
    private String createdAt;

    @NotBlank
    @Size(max = 20)
    @Column(name = "UPDATED_AT", nullable = false)
    private String updatedAt;
}
