package ru.nsu.projects.malaeva.operation;

import java.util.Map;

public class AnyQuantifier extends Quantifier {

    @Override
    public boolean getValue(Map<String, Boolean> atomsValue) {
        return false;
    }


    @Override
    public String toString() {
        return "A" + getVariableName() + " (" + getArgument().toString() + ")";
    }
}