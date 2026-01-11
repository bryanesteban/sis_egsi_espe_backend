package com.espe.ListoEgsi.domain.dto.question;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhaseDTO {

    private UUID idPhase;
    private UUID idProcess;
    private String questionaryCode;
    private String responsibles;
    private String status;
}
