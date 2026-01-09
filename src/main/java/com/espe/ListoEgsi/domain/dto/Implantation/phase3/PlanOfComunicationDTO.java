package com.espe.ListoEgsi.domain.dto.Implantation.phase3;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanOfComunicationDTO {

    private UUID idPlanComunication;
    private UUID idProcess;
    private String background;
    private String objetiveOfPlan;
    private String specificObjetives;
    private String planDescription;
    private String scopePlan;
    private String desingPlan;
    private String materialsAndChannels;
    private String strategiesPlan;
    private String needsIdentifications;
    private String rolesAndResponsabilities;
    private String resultsAndAchievements;
    private String scheduleActivities;
    private String glosaryTerms;
    private String documentsOfReference;
    private String state;

}
