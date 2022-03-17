package ru.nsu.projects.malaeva.core;

public interface OperationStackItem {
    int getPriorityLevel();
    StackItemType getStackItemType();
}
