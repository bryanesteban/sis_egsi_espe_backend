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

    @Size(max = 100)
    @Column(name = "NAME_RESPONSIBLE")
    private String nameResponsible;

    @Size(max = 50)
    @Column(name = "STATUS_SIGN")
    private String statusSign;

    @Size(max = 20)
    @Column(name = "CREATED_AT")
    private String createdAt;

    @Size(max = 20)
    @Column(name = "UPDATED_AT")
    private String updatedAt;
}
