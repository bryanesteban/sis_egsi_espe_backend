package com.espe.ListoEgsi.service.Inplantation.phase8;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.Implantation.phase8.DeclarationAplicationSoaDTO;

public interface DeclarationAplicationSoaService {

    List<DeclarationAplicationSoaDTO> findAllDeclarationAplicationSoas();
    DeclarationAplicationSoaDTO getDeclarationAplicationSoa (UUID idDeclarationAplicationSoa);
    DeclarationAplicationSoaDTO saveDeclarationAplicationSoa (DeclarationAplicationSoaDTO declarationAplicationSoaSubmit);
    DeclarationAplicationSoaDTO updateDeclarationAplicationSoa (DeclarationAplicationSoaDTO declarationAplicationSoaUpdate);
    void deleteDeclarationAplicationSoa (UUID idDeclarationAplicationSoa);
}
