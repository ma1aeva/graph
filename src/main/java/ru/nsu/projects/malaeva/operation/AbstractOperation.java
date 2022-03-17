package ru.nsu.projects.malaeva.operation;

import ru.nsu.projects.malaeva.core.Operation;
import ru.nsu.projects.malaeva.core.OperationStackItem;
import ru.nsu.projects.malaeva.core.StackItemType;

public abstract class AbstractOperation implements OperationStackItem, Operation {

    @Override
    public StackItemType getStackItemType() {
        return StackItemType.OPERATION;
    }
}
