package com.espe.ListoEgsi.domain.dto.question;

import java.util.UUID;

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
public class PhaseDTO {

    private UUID idPhase;

    private UUID idProcess;

    @NotBlank
    @Size(max = 50)
    private String questionaryCode;

    @NotBlank
    @Size(max = 500)
    private String responsibles;

    @NotBlank
    @Size(max = 50)
    private String status;
}
