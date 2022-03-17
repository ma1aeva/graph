package ru.nsu.projects.malaeva.operation;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.projects.malaeva.core.Formula;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
}
