package ru.nsu.projects.malaeva.core;


import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public interface Formula {
    boolean getValue(Map<String, Map<String, Boolean>> atomsValue);
    Formula withoutQuantifiers(Set<String> constants, Set<String> specialConstants);
    Formula replaceVariables(String variable, String replacement);
    Set<String> getConstants();
   void fillAtoms(@NotNull Map<String, Set<String>> atoms);
}