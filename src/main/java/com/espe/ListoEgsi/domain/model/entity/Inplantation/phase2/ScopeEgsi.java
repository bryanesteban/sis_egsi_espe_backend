package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase2;

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
@Table(name = "SCOPE")
public class ScopeEgsi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_SCOPE", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idScope;

    // Relación con ProcessEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false, referencedColumnName = "ID_PROCESS")
    private ProcessEgsi process;

    @NotBlank
    @Size(max = 500)
    @Column(name = "SCOPE_DESCRIPTION", nullable = false)
    private String scopeDescription;

    @NotBlank
    @Size(max = 500)
    @Column(name = "OBJETIVE_SCOPE", nullable = false)
    private String objetiveScope;

    @NotBlank
    @Size(max = 500)
    @Column(name = "USERS_SCOPE", nullable = false)
    private String usersScope;

    @NotBlank
    @Size(max = 500)
    @Column(name = "REVIEW_AND_UPDATE", nullable = false)
    private String reviewAndUpdate;

    @NotBlank
    @Size(max = 500)
    @Column(name = "REFERENCE_DOCUMENTS", nullable = false)
    private String referenceDocuments;

    @NotBlank
    @Size(max = 500)
    @Column(name = "SCOPE_DEFINITION", nullable = false)
    private String scopeDefinition;

    @NotBlank
    @Size(max = 2000)
    @Column(name = "PROCESS_AND_SERVICES", nullable = false)
    private String processAndServices;

    @NotBlank
    @Size(max = 500)
    @Column(name = "ORGANIZATION_UNITS", nullable = false)
    private String organizationUnits;

    @NotBlank
    @Size(max = 500)
    @Column(name = "UBICATION_DESCRIPTION", nullable = false)
    private String ubicationDescription;

    @NotBlank
    @Size(max = 2000)
    @Column(name = "UBICATION_TABLE", nullable = false)
    private String ubicationTable;

    @NotBlank
    @Size(max = 500)
    @Column(name = "NETWORKS_AND_INFRAESTRUCTURE_TI", nullable = false)
    private String networksAndInfraestructureTi;

    @NotBlank
    @Size(max = 500)
    @Column(name = "EXCLUSION", nullable = false)
    private String exclusion;

    @NotBlank
    @Size(max = 20)
    @Column(name = "STATE", nullable = false)
    private String state;
}
