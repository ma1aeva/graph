package ru.nsu.projects.malaeva.operation;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import ru.nsu.projects.malaeva.core.Atom;
import ru.nsu.projects.malaeva.core.Formula;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
public class Predicate implements Atom {
    private final String predicateName;
    private final String argumentName;
    private boolean negative;

    @Override
    public Set<String> getConstants() {
        return Set.of(argumentName);
    }

    // TODO подправить функцию хэш кода, эта работает не очень хорошо (я не знаю, как)
    // Но она важна, за счет нее мы сравниваем предикаты м/у собой
    @Override
    public int hashCode() {
        return ((int)Math.pow(predicateName.hashCode() % 33353, 2))
                * (argumentName.hashCode() % 5555)  * ((negative) ? -1 : 1);
    }

    public Predicate(Predicate predicate) {
        this.argumentName = predicate.argumentName;
        this.predicateName = predicate.predicateName;
        this.negative = predicate.negative;
    }

    @Override
    public boolean getValue(Map<String, Map<String, Boolean>> atomsValue) {
        return atomsValue.get(predicateName).get(argumentName);
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) return true;
        else if (object instanceof Predicate cast) {
            return cast.predicateName.equals(this.predicateName) &&
                    cast.argumentName.equals(this.argumentName) &&
                    cast.negative == this.negative;
        }
        return false;
    }

    @Override
    public void fillAtoms(@NotNull Map<String, Set<String>> atoms) {
        Set<String> predicateArguments;
        if (atoms.get(predicateName) == null) {
            predicateArguments = new HashSet<>();
            atoms.put(predicateName, predicateArguments);
        }
        else predicateArguments = atoms.get(predicateName);
        predicateArguments.add(argumentName);
    }

    @Override
    public String toString() {
        return (negative ? "!" : "") + predicateName + "(" + argumentName + ")";
    }

    @Override
    public Formula withoutQuantifiers(Set<String> constants, Set<String> specialConstants) {
        return new Predicate(predicateName, argumentName);
    }

    @Override
    public Formula replaceVariables(String variable, String replacement) {
        return new Predicate(predicateName, variable.equals(argumentName) ? replacement : argumentName);
    }
}