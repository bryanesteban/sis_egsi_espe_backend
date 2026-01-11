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
public class ResponsibleSigningDTO {

    private UUID idResponsible;
    private UUID idPhase;
    private String nameResponsible;
    private String statusSign;
    private String createdAt;
    private String updatedAt;
}
