package com.espe.ListoEgsi.service.Inplantation.phase8;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.Implantation.phase8.AnexoEgsiDTO;

public interface AnexoEgsiService {

    List<AnexoEgsiDTO> findAllAnexoEgis();
    AnexoEgsiDTO getAnexoEgsi (UUID idAnexoEgsi);
    AnexoEgsiDTO saveAnexoEgsi (AnexoEgsiDTO anexoEgsiSubmited);
    AnexoEgsiDTO updateAnexoEgsi (AnexoEgsiDTO anexoEgsiUpdate);
    void deleteAnexoEgsi (UUID idAnexoEgsi);
}
