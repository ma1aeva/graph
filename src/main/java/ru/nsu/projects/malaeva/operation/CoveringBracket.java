package ru.nsu.projects.malaeva.operation;

import ru.nsu.projects.malaeva.core.OperationStackItem;
import ru.nsu.projects.malaeva.core.StackItemType;

public class CoveringBracket implements OperationStackItem {

    @Override
    public StackItemType getStackItemType() {
        return StackItemType.OPENING_BRACKET;
    }

    @Override
    public int getPriorityLevel() {
        return 0;
    }
}
