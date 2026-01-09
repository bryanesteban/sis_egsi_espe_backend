package com.espe.ListoEgsi.domain.dto.Implantation.phase2;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScopeEgsiDTO {

    private UUID idScope;
    private UUID idProcess;
    private String scopeDescription;
    private String objetiveScope;
    private String usersScope;
    private String reviewAndUpdate;
    private String referenceDocuments;
    private String scopeDefinition;
    private String processAndServices;
    private String organizationUnits;
    private String ubicationDescription;
    private String ubicationTable;
    private String networksAndInfraestructureTi;
    private String exclusion;
    private String state;

}
