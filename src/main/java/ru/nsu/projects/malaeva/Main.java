package ru.nsu.projects.malaeva;

import ru.nsu.projects.malaeva.core.Formula;
import ru.nsu.projects.malaeva.core.Parser;
import ru.nsu.projects.malaeva.core.ParserImpl;

import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Parser parser = new ParserImpl();
        Formula formula = parser.parseFormula("Ax(f(x) & !s(f) @ k(i) @ s(m) & Eg(f(g)))");
        System.out.println(formula);
        System.out.println(formula.getConstants());
    }
}