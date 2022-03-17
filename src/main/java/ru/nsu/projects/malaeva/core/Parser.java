package ru.nsu.projects.malaeva.core;

import ru.nsu.projects.malaeva.core.Formula;

public interface Parser {
    Formula parseFormula(String formulaString);
}