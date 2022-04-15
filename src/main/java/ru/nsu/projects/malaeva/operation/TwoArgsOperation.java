package ru.nsu.projects.malaeva.operation;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import ru.nsu.projects.malaeva.core.Formula;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
public abstract class TwoArgsOperation extends AbstractOperation {
    private Formula firstOperand;
    private Formula secondOperand;

    @Override
    public int getSlotNumber() {
        return 2;
    }

    @Override
    public void fillSlots(List<Formula> formulaList) {
        if (formulaList.size() != 2) {
            throw new RuntimeException("Попытка запихнуть в двухместный оператор некорректное количество аргументов");
        }
        firstOperand = formulaList.get(0);
        secondOperand = formulaList.get(1);
    }

    @Override
    public Set<String> getConstants() {
        Set<String> constants = new HashSet<>(firstOperand.getConstants());
        constants.addAll(secondOperand.getConstants());
        return constants;
    }

    @Override
    public Formula withoutQuantifiers(Set<String> constants, Set<String> specialConstants) {
        TwoArgsOperation twoArgsOperation = createOperation();
        twoArgsOperation.setFirstOperand(getFirstOperand().withoutQuantifiers(constants, specialConstants));
        twoArgsOperation.setSecondOperand(getSecondOperand().withoutQuantifiers(constants, specialConstants));
        return twoArgsOperation;
    }

    protected abstract TwoArgsOperation createOperation();

    @Override
    public Formula replaceVariables(String variable, String replacement) {
        TwoArgsOperation twoArgsOperation = createOperation();
        twoArgsOperation.setFirstOperand(getFirstOperand().replaceVariables(variable, replacement));
        twoArgsOperation.setSecondOperand(getSecondOperand().replaceVariables(variable, replacement));
        return twoArgsOperation;
    }

    @Override
    public void fillAtoms(@NotNull Map<String, Set<String>> atoms) {
        getFirstOperand().fillAtoms(atoms);
        getSecondOperand().fillAtoms(atoms);
    }
}
