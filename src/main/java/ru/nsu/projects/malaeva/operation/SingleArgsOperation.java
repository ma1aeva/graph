package ru.nsu.projects.malaeva.operation;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import ru.nsu.projects.malaeva.core.Formula;

import java.util.List;
import java.util.Map;
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

    @Override
    public Formula withoutQuantifiers(Set<String> constants, Set<String> specialConstants) {
        SingleArgsOperation singleArgsOperation = createOperation();
        singleArgsOperation.setArgument(getArgument().withoutQuantifiers(constants, specialConstants));
        return singleArgsOperation;
    }

    protected abstract SingleArgsOperation createOperation();

    @Override
    public Formula replaceVariables(String variable, String replacement) {
        SingleArgsOperation singleArgsOperation = createOperation();
        singleArgsOperation.setArgument(getArgument().replaceVariables(variable, replacement));
        return singleArgsOperation;
    }

    @Override
    public void getAtoms(@NotNull Map<String, Set<String>> atoms) {
        getArgument().getAtoms(atoms);
    }
}
