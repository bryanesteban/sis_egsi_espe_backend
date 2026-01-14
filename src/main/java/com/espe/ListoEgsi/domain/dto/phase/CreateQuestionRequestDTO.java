package com.espe.ListoEgsi.domain.dto.phase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequestDTO {
    
    @NotBlank(message = "El título de la pregunta es obligatorio")
    @Size(max = 500, message = "El título no puede exceder 500 caracteres")
    private String title;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;
    
    @NotBlank(message = "El tipo de input es obligatorio")
    @Pattern(regexp = "TEXTO|DATE|TABLA", message = "El tipo de input debe ser TEXTO, DATE o TABLA")
    private String inputType;
    
    @NotNull(message = "Debe indicar si la pregunta es obligatoria")
    private Boolean required;
    
    @Size(max = 500, message = "El placeholder no puede exceder 500 caracteres")
    private String placeholder;
    
    private Integer maxLength;
    
    private String tableConfig;
    
    @NotNull(message = "El orden de la pregunta es obligatorio")
    private Integer order;
}
