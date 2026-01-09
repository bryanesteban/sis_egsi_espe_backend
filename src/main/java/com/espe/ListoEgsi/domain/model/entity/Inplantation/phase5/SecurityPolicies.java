package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase5;

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
@Table(name = "SECURITY_POLICIES")
public class SecurityPolicies {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_SECURITY_POLICIES", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idSecurityPolicies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false, referencedColumnName = "ID_PROCESS")
    private ProcessEgsi process;

    @Size(max = 1000)
    @Column(name = "BACKGROUNDS")
    private String backgrounds;

    @Size(max = 1000)
    @Column(name = "POLICY_OBJETIVE")
    private String policyObjetive;

    @Size(max = 1000)
    @Column(name = "DESCRIPTION_POLICY")
    private String descriptionPolicy;

    @Size(max = 1000)
    @Column(name = "DECLARATION_OBJETIVE_SECURITY")
    private String declarationObjetiveSecurity;

    @Size(max = 1000)
    @Column(name = "ROLES_AND_RESPONSABILITIES")
    private String rolesAndResponsabilities;

    @Size(max = 1000)
    @Column(name = "SCOPE_AND_USERS")
    private String scopeAndUsers;

    @Size(max = 1000)
    @Column(name = "COMUNICATION_POLICY")
    private String comunicationPolicy;

    @Size(max = 1000)
    @Column(name = "EXCEPTION_SANCTIONS")
    private String exceptionSanctions;

    @Size(max = 1000)
    @Column(name = "GLOSARY_TERMS_TABLE")
    private String glosaryTermsTable;

    @Size(max = 500)
    @Column(name = "REFERENCE_DOCUMENTS")
    private String referenceDocuments;
}
