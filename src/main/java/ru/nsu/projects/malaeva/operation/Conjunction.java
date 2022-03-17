package ru.nsu.projects.malaeva.operation;

import java.util.Map;

public class Conjunction extends TwoArgsOperation {

    @Override
    public boolean getValue(Map<String, Boolean> atomsValue) {
        return false;
    }

    @Override
    public String toString() {
        return "(" + getFirstOperand().toString() + " & " + getSecondOperand().toString() + ")";
    }

    @Override
    public int getPriorityLevel() {
        return 7;
    }
}
