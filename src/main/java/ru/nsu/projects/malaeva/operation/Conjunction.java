package ru.nsu.projects.malaeva.operation;


import java.util.Map;

public class Conjunction extends TwoArgsOperation {

    @Override
    public boolean getValue(Map<String, Map<String, Boolean>> atomsValue) {
        return getFirstOperand().getValue(atomsValue) && getSecondOperand().getValue(atomsValue);
    }

    @Override
    public java.lang.String toString() {
        return "(" + getFirstOperand().toString() + " ∧ " + getSecondOperand().toString() + ")";
    }

    @Override
    public int getPriorityLevel() {
        return 7;
    }

    @Override
    protected TwoArgsOperation createOperation() {
        return new Conjunction();
    }
}
