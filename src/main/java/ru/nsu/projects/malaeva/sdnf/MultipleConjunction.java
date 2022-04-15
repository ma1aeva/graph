package ru.nsu.projects.malaeva.sdnf;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.projects.malaeva.operation.Predicate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class MultipleConjunction {
    private Set<Predicate> predicates = new HashSet<>();


    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) return true;
        if (object instanceof MultipleConjunction multipleConjunction) {
            return multipleConjunction.getPredicates().equals(predicates);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return predicates.hashCode();
    }

    @Override
    public String toString() {
        return predicates.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(" ∧ "));
    }
}