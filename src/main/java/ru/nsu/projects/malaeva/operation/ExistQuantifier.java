package ru.nsu.projects.malaeva.operation;

import java.util.Map;

public class ExistQuantifier extends Quantifier {

    @Override
    public String toString() {
        return "E" + getVariableName() + " (" + getArgument().toString() + ")";
    }

    @Override
    public boolean getValue(Map<String, Boolean> atomsValue) {
        return false;
    }
}
