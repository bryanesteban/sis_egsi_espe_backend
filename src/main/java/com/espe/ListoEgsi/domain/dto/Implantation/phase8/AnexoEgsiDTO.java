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
public class AnexoEgsiDTO {
    private UUID idAnexoEgsi;
    private UUID idDeclaration;
    private String item;
    private String section;
    private String description;
    private String actualState;
    private String applies;
    private String selectionJustify;
    private String observation;

}
