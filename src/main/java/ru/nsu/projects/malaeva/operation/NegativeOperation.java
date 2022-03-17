package ru.nsu.projects.malaeva.operation;

import java.util.Map;

public class NegativeOperation extends SingleArgsOperation {

    @Override
    public boolean getValue(Map<String, Boolean> atomsValue) {
        return false;
    }

    @Override
    public String toString() {
        return "!(" + getArgument().toString() + ")";
    }

    @Override
    public int getPriorityLevel() {
        return 11;
    }
}
