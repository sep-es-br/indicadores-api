package br.gov.es.indicadores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerItemDto {
    private String name;
    private String description;
    private String icon;
    private String modelName;
    private String modelNameInPlural;
}
