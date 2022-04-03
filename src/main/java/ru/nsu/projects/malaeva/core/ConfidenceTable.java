package ru.nsu.projects.malaeva.core;

import ru.nsu.projects.malaeva.sdnf.MultipleConjunction;

import java.util.Map;
import java.util.Set;

public interface ConfidenceTable {
    void fillTable(Formula formula);
//    boolean isSubformula(ConfidenceTable confidenceTable);
    boolean comparable(ConfidenceTable confidenceTable);

    Map<String, Set<String>> getPredicates();
    int getPredicateCount();

    Set<MultipleConjunction> getSdnfConjunctions();
    Set<MultipleConjunction> getAllConjunctions();
}