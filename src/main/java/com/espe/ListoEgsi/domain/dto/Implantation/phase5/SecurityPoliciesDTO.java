package com.espe.ListoEgsi.domain.dto.Implantation.phase5;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityPoliciesDTO {

    private UUID idSecurityPolicies;
    private UUID idProcess;
    private String backgrounds;
    private String policyObjetive;
    private String descriptionPolicy;
    private String declarationObjetiveSecurity;
    private String rolesAndResponsabilities;
    private String scopeAndUsers;
    private String comunicationPolicy;
    private String exceptionSanctions;
    private String glosaryTermsTable;
    private String referenceDocuments;
}
