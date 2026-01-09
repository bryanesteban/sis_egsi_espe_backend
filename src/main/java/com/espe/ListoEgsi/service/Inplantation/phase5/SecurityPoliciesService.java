package com.espe.ListoEgsi.service.Inplantation.phase5;

import java.util.List;
import java.util.UUID;

import com.espe.ListoEgsi.domain.dto.Implantation.phase5.SecurityPoliciesDTO;

public interface SecurityPoliciesService {

    List<SecurityPoliciesDTO> findAllSecurityPolicies();
    SecurityPoliciesDTO getSecurityPolicies (UUID idSecurityPolicies);
    SecurityPoliciesDTO saveSecurityPolicies (SecurityPoliciesDTO securityPoliciesSubmit);
    SecurityPoliciesDTO updateSecurityPolicies (SecurityPoliciesDTO securityPoliciesUpdate);
    void deleteSecurityPolicies (UUID idSecurityPolicies);

}
