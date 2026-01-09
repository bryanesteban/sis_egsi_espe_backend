package com.espe.ListoEgsi.service.Inplantation.phase2;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.Implantation.phase2.ScopeEgsiDTO;

public interface ScopeService {

    List<ScopeEgsiDTO> findAllScopes();
    ScopeEgsiDTO getScope(UUID idScope);
    ScopeEgsiDTO saveScope(ScopeEgsiDTO scopeSubmit);
    ScopeEgsiDTO updateScope(ScopeEgsiDTO scopeUpdate);
    void deleteScope (UUID idScope);
    


}
