package ru.nsu.projects.malaeva.core;

import java.util.List;

public interface Operation extends Formula {
    int getSlotNumber();
    void fillSlots(List<Formula> formulaList);
}