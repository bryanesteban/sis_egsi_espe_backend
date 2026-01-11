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
    private String name;
    private String description;
    private String dateBegin;
    private String dateEnd;
    private String active;
    private String currentPhase;
    private String createdBy;
}

