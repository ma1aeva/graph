package ru.nsu.projects.malaeva.core;

import java.util.Map;
import java.util.Set;

public interface Formula {
    boolean getValue(Map<String, Boolean> atomsValue);
//    Formula withoutQuantifiers();
    Set<String> getConstants();
    String toString();
}