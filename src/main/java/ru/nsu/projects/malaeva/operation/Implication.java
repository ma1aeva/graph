package ru.nsu.projects.malaeva.operation;

import java.util.Map;

public class Implication extends TwoArgsOperation {
    @Override
    public boolean getValue(Map<String, Map<String, Boolean>> atomsValue) {
        return !getFirstOperand().getValue(atomsValue) || getSecondOperand().getValue(atomsValue);
    }

    @Override
    public java.lang.String toString() {
        return "(" + getFirstOperand().toString() + " → " + getSecondOperand().toString() + ")";
    }

    @Override
    public int getPriorityLevel() {
        return 3;
    }

    @Override
    protected TwoArgsOperation createOperation() {
        return new Implication();
    }
}
