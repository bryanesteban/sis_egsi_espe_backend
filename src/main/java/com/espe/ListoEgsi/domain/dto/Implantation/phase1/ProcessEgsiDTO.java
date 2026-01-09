package com.espe.ListoEgsi.domain.dto.Implantation.phase1;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessEgsiDTO {

    private UUID idProcess;
    private String relatedProgram;
    private String projectLeader;
    private String dateBegin;
    private String dateEnd;
    private String problem;
    private String justification;
    private String projectDescription;
    private String benefits;
    private String generalObjective;
    private String beneficiaries;
    private String implementationPeriod;

}
