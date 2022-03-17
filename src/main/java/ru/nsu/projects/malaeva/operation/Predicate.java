package ru.nsu.projects.malaeva.operation;

import lombok.Setter;
import ru.nsu.projects.malaeva.core.Atom;

import java.util.Map;
import java.util.Set;

@Setter
public class Predicate implements Atom {
    private String predicateName;
    private String argumentName;

    @Override
    public Set<String> getConstants() {
        return Set.of(argumentName);
    }

    @Override
    public boolean getValue(Map<String, Boolean> atomsValue) {
        return false;
    }

    @Override
    public String toString() {
        return predicateName + "(" + argumentName + ")";
    }
}