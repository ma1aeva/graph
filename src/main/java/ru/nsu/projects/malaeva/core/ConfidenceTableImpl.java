package ru.nsu.projects.malaeva.core;

import ru.nsu.projects.malaeva.operation.Predicate;
import ru.nsu.projects.malaeva.sdnf.MultipleConjunction;

import java.util.*;

public class ConfidenceTableImpl implements ConfidenceTable {

    private Predicate[] tableHeader;
    private Map<String, Set<String>> predicates;
    private boolean[][] tableBody;
    private int tableRowCount;
    private boolean[] results;
    private Formula formula;

    private void initTable(Formula formula) {

        this.formula = formula;
        this.predicates = new HashMap<>();
        // Заполняем предикаты, которые нам известны
        this.formula.fillAtoms(this.predicates);

        //TODO проверка на то, есть ли здесь константы
        // Подготавливаем формулу (Убираем кванторы)


        // Теперь, имея на руках данные о предикатах (название предиката и сет значений), мы хотим посчитать, сколько их
        // Эта логика работает только в том случае, если предикат одноместный
        int predicateNumbers = (int) predicates.values().stream()
                .mapToLong(Collection::size)
                .sum();

        this.tableRowCount = 1 << predicateNumbers;
        // Теперь мы инициализируем таблицы (заголовок с пердикатами, теблицу значений, таблицу результатов)
        this.tableHeader = new Predicate[predicateNumbers];
        this.tableBody = new boolean[predicateNumbers][tableRowCount];
        this.results = new boolean[this.tableRowCount];
    }

    @Override
    public void fillTable(Formula formula) {
        initTable(formula);

        // Заполнение заголовка (таблицы с предикатами)
        int index = 0;
        for (Map.Entry<String, Set<String>> predicate : predicates.entrySet()) {
            for (String constant : predicate.getValue()) {
                Predicate predicateInstance = new Predicate(predicate.getKey(), constant);
                this.tableHeader[index] = predicateInstance;
                index++;
            }
        }

        // Теперь мы заполняем значения предикатов в основную таблицу и получаем значение формулы при таких вводных
        for (int row = 0; row < tableRowCount; row++) {
            Map<String, Map<String, Boolean>> predicateValues = new HashMap<>();
            int remainder = row;
            for (int column = 0; column < tableHeader.length; column++) {
                int columnBinaryRepresentation = 1 << (tableHeader.length - column - 1);
                if (remainder >= columnBinaryRepresentation) {
                    tableBody[column][row] = true;
                    remainder -= columnBinaryRepresentation;
                }

                predicateValues.computeIfAbsent(tableHeader[column].getPredicateName(), k -> new HashMap<>());
                predicateValues.get(tableHeader[column].getPredicateName())
                        .put(tableHeader[column].getArgumentName(), tableBody[column][row]);
            }
            results[row] = this.formula.getValue(predicateValues);
        }
    }

    @Override
    public Set<MultipleConjunction> getSdnfConjunctions() {
        Set<MultipleConjunction> conjunctionList = new HashSet<>();
        for (int row = 0; row < tableRowCount; row++) {
            if (results[row]) {
                MultipleConjunction multipleConjunction = new MultipleConjunction();
                for (int column = 0; column < tableHeader.length; column++) {
                    // Копируем предикат из таблицы в конъюнкт
                    Predicate predicateCopy = new Predicate(tableHeader[column]);
                    predicateCopy.setNegative(!tableBody[column][row]);
                    multipleConjunction.getPredicates().add(predicateCopy);
                }
                conjunctionList.add(multipleConjunction);
            }
        }
        return conjunctionList;
    }

    @Override
    public Map<String, Set<String>> getPredicates() {
        return predicates;
    }

    @Override
    public int getPredicateCount() {
        return tableHeader.length;
    }

    //TODO - вывести в отдельный метод
    @Override
    public Set<MultipleConjunction> getAllConjunctions() {
        Set<MultipleConjunction> conjunctionList = new HashSet<>();
        for (int row = 0; row < tableRowCount; row++) {
                MultipleConjunction multipleConjunction = new MultipleConjunction();
                for (int column = 0; column < tableHeader.length; column++) {
                    // Копируем предикат из таблицы в конъюнкт
                    Predicate predicateCopy = new Predicate(tableHeader[column]);
                    predicateCopy.setNegative(tableBody[column][row]);
                    multipleConjunction.getPredicates().add(predicateCopy);
                }
                conjunctionList.add(multipleConjunction);
        }
        return conjunctionList;
    }
}