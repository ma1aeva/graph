package ru.nsu.projects.malaeva.operation;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.projects.malaeva.core.Formula;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public abstract class SingleArgsOperation extends AbstractOperation {
    private Formula argument;

    @Override
    public void fillSlots(List<Formula> formulaList) {
        if (formulaList.size() != 1) {
            throw new RuntimeException("Кто-то пытается скормить SingleArgsOperation" +
                    " некорректное количество аргументов: " + formulaList.size());
        }
        argument = formulaList.get(0);
    }

    @Override
    public Set<String> getConstants() {
        return argument.getConstants();
    }

    @Override
    public int getSlotNumber() {
        return 1;
    }
}
