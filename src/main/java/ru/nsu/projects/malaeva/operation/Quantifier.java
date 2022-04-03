package ru.nsu.projects.malaeva.operation;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.projects.malaeva.core.Formula;
import ru.nsu.projects.malaeva.core.OperationNotSupportedException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public abstract class Quantifier extends SingleArgsOperation {
    private java.lang.String variableName;


    @Override
    protected SingleArgsOperation createOperation() {
        return null;
    }

    @Override
    public int getPriorityLevel() {
        return 10;
    }

    @Override
    public Set<String> getConstants() {
        return super.getConstants().stream()
                .filter(s -> !s.equals(variableName))
                .collect(Collectors.toSet());
    }

    @Override
    public Formula replaceVariables(String variable, String replacement) {
        throw new OperationNotSupportedException("Невозможно применить операцию изменения переменной к квантору," +
                " не поддерживаются вложенные кванторв");
    }

    // Метод для получение подформул, с измененными на константы переменными
    public List<Formula> getSubformulaVariablesReplacement(Set<java.lang.String> constants, Set<java.lang.String> specialConstants) {
        return Stream.concat(
                constants.stream().map(constant -> getArgument().replaceVariables(getVariableName(), constant)),
                specialConstants.stream().map(constant -> getArgument().replaceVariables(getVariableName(), constant))
        ).toList();
    }

    @Override
    public boolean getValue(Map<String, Map<String, Boolean>> atomsValue) {
        throw new OperationNotSupportedException("Невозможно получить значение формулы, содержащей кванторы");
    }
}