package ru.nsu.projects.malaeva;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomEdge {

    public enum ProbabilityRelation {
        CORRECT,
        INCORRECT
    }

    @Getter
    private final ProbabilityRelation relation;

    @Override
    public String toString() {
        return "";
    }
}
