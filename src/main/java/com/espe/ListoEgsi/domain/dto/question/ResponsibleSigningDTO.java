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
public class ResponsibleSigningDTO {

    private UUID idResponsible;

    private UUID idPhase;

    @NotBlank
    @Size(max = 100)
    private String nameResponsible;

    @NotBlank
    @Size(max = 50)
    private String statusSign;

    @NotBlank
    @Size(max = 20)
    private String createdAt;

    @NotBlank
    @Size(max = 20)
    private String updatedAt;
}
