package com.espe.ListoEgsi.domain.dto.phase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EgsiQuestionDTO {
    private String idQuestion;
    private String title;
    private String description;
    private String inputType;
    private Boolean required;
    private String placeholder;
    private Integer maxLength;
    
    @JsonRawValue
    private String tableConfig;
    
    private Integer order;
}
