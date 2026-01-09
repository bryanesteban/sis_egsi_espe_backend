package com.espe.ListoEgsi.domain.dto.Implantation.phase4;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanInternalEvaluationDTO {

    private UUID idPlanInternalEval;
    private UUID idProcess;
    private String background;
    private String documentObjetive;
    private String scopePlan;
    private String criterionPlan;
    private String methodologyPlan;
    private String cronogramPlan;
    private String resourcePlan;
    private String documentationFindingsPlan;
    private String documentationFindingsPlanTable;
    private String resultsPlan;
    private String glosaryTermsTable;
    private String documentsReference;
    private String controlVersion;
    private String changeHistorial;
    private String scheduleWorking;
    

}
