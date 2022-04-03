package ru.nsu.projects.malaeva.operation;

import java.util.Map;

public class NegativeOperation extends SingleArgsOperation {

    @Override
    public boolean getValue(Map<String, Map<String, Boolean>> atomsValue) {
        return !getArgument().getValue(atomsValue);
    }

    @Override
    public String toString() {
        return "!(" + getArgument().toString() + ")";
    }

    @Override
    public int getPriorityLevel() {
        return 11;
    }

    @Override
    protected SingleArgsOperation createOperation() {
        return new NegativeOperation();
    }
}
