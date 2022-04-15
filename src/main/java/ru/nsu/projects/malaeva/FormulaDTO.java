package ru.nsu.projects.malaeva;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FormulaDTO {
    private String formula;
    private double probability;
}
