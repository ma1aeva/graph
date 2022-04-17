package ru.nsu.projects.malaeva.operation;


import ru.nsu.projects.malaeva.core.Formula;
import ru.nsu.projects.malaeva.core.OperationNotSupportedException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExistQuantifier extends Quantifier {

    @Override
    public String toString() {
        return "∃" + getVariableName() + " (" + getArgument().toString() + ")";
    }

    @Override
    public boolean getValue(Map<String, Map<String, Boolean>> atomsValue) {
        throw new OperationNotSupportedException("Невозможно получить значение формулы, имеющий в составе квантор");
    }

    @Override
    public Formula withoutQuantifiers(Set<String> constants, Set<String> specialConstants) {
        if (specialConstants.isEmpty()) {
            throw new OperationNotSupportedException(" ");
        }
        List<Formula> replacements = getSubformulaVariablesReplacement(constants, specialConstants);
        //TODO если у нас недостаточно формул
        Formula result = replacements.get(0);
        for (int i = 1; i < replacements.size(); i++) {
            Disjunction formulasUnion = new Disjunction();
            formulasUnion.setFirstOperand(result);
            formulasUnion.setSecondOperand(replacements.get(i));
            result = formulasUnion;
        }
        return result;
    }
}