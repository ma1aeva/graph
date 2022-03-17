package ru.nsu.projects.malaeva.operation;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class Quantifier extends SingleArgsOperation {
    private String variableName;

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
}