package com.espe.ListoEgsi.domain.dto.Implantation.phase8;

import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeclarationAplicationSoaDTO {

    private UUID idDeclaration;
    private UUID idProcess;
    private String institute;
    private String numberController;
    private String levelConfidentiality;
    private String documentResponsible;

}
