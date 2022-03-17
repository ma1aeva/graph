package ru.nsu.projects.malaeva.operation;

import ru.nsu.projects.malaeva.core.OperationStackItem;
import ru.nsu.projects.malaeva.core.StackItemType;

public class ClosingBracket implements OperationStackItem {

    @Override
    public StackItemType getStackItemType() {
        return StackItemType.CLOSING_BRACKET;
    }

    @Override
    public int getPriorityLevel() {
        return 0;
    }
}