package ru.nsu.projects.malaeva.core;

import ru.nsu.projects.malaeva.operation.*;

import java.util.*;

public class ParserImpl implements Parser {
    private final Stack<OperationStackItem> operationStack;
    private final Stack<Formula> formulaStack;

    public ParserImpl() {
        this.operationStack = new Stack<>();
        this.formulaStack = new Stack<>();
    }

    private void fillOperationSlots(Operation operation) {
        if (formulaStack.size() < operation.getSlotNumber()) {
            throw new ParseFormulaException("Слишком мало операндов для составления формулы");
        }

        List<Formula> arguments = new ArrayList<>();
        for (int i = 0; i < operation.getSlotNumber() && !formulaStack.isEmpty(); i++) {
            arguments.add(formulaStack.pop());
        }
        Collections.reverse(arguments);
        operation.fillSlots(arguments);
    }

    private void handleOpeningBracket(OperationStackItem operationStackItem) {
        operationStack.push(operationStackItem);
    }

    private void handleClosingBracket(OperationStackItem operationStackItem) {
        try {
            while (!(operationStack.peek().getStackItemType() == StackItemType.OPENING_BRACKET)) {
                OperationStackItem betweenBracketsItem = operationStack.pop();
                if (!(betweenBracketsItem.getStackItemType() == StackItemType.OPERATION)) {
                    throw new ParseFormulaException("Мы встретили что-то отличное от оператора при раскрутке стка из скобок");
                }
                fillOperationSlots((Operation) betweenBracketsItem);
                formulaStack.push((Operation) betweenBracketsItem);
            }
            operationStack.pop();
        } catch (EmptyStackException emptyStackException) {
            throw new ParseFormulaException("Не удалось найти открывающуюся скобку при обработке закрывающейся");
        }
    }

    private void handleOperation(OperationStackItem operation) {
        // Если стек пустой, то просто кладем в стек
        if (operationStack.isEmpty()) {
            operationStack.push(operation);
        }
        // Если же стек полный, то проводим работу с приоритетами, вытесняя операции с большим приоритетом
        else {
            // Вытаскиваем все элементы из стека с большим приоритетом
            while (!operationStack.isEmpty() &&
                    operationStack.peek().getStackItemType() == StackItemType.OPERATION
                    && operationStack.peek().getPriorityLevel() > operation.getPriorityLevel()) {

                OperationStackItem highPriorityOperation = operationStack.pop();
                fillOperationSlots((Operation) highPriorityOperation);
                formulaStack.push((Operation) highPriorityOperation);
            }
            // Кладем новую операцию в стек
            operationStack.push(operation);
        }
    }

    private void processOperation(OperationStackItem operationStackItem) {
        switch (operationStackItem.getStackItemType()) {
            case OPERATION -> handleOperation(operationStackItem);
            case OPENING_BRACKET -> handleOpeningBracket(operationStackItem);
            case CLOSING_BRACKET -> handleClosingBracket(operationStackItem);
        }
    }

    private void processAtom(Atom atom) {
        formulaStack.push(atom);
    }

    private Formula endParsing() {
        Operation residualOperation = null;
        while (!operationStack.isEmpty()) {
            OperationStackItem stackItem = operationStack.pop();
            if (stackItem.getStackItemType() != StackItemType.OPERATION) {
                throw new ParseFormulaException("При окончании парсинга в стеке был найден элемент, отличный от операции");
            }
            residualOperation = (Operation) stackItem;
            fillOperationSlots(residualOperation);
        }
        return residualOperation;
    }


    // TODO Name parser (strategy)
    // Возврящает номер последнего символа переменной в массиве
    int parseName(char[] formulaCharArray, int firstSymbol) {
        // Если у нас первый символ - не буква, то возврящаем -1
        if (firstSymbol >= formulaCharArray.length || !Character.isLetter(formulaCharArray[firstSymbol]))
            return -1;

            // Далее для всех символов, начиная со второго, делаем проверку
            for (int i = firstSymbol + 1; i < formulaCharArray.length; i++) {
                if (!Character.isLetter(formulaCharArray[i]) && !Character.isDigit(formulaCharArray[i])) {
                    return i - firstSymbol;
                }
            }
            return formulaCharArray.length - firstSymbol;
        }

        // Правила: 1. Пробелы не считаются
        //          2. Чтобы при использовании кванторов, переменная должна отделяться от тела скобками
        //          3. Квантор - одноместная функция

        @Override
        public Formula parseFormula(String formulaString) {

            // Убираем все пробелы
            char[] formulaCharArray = formulaString.replace(" ", "").toCharArray();

            int currentIndex = 0;
            while (currentIndex < formulaCharArray.length) {

                // Если прочитанная операция - это квантор
                if (formulaCharArray[currentIndex] == 'A' || formulaCharArray[currentIndex] == 'E') {
                    int lastVariableNameLength = parseName(formulaCharArray, currentIndex + 1);
                    if (lastVariableNameLength == -1)
                        throw new ParseFormulaException("Не удаслось распарсить название переменной");

                    Quantifier quantifier = (formulaCharArray[currentIndex] == 'A') ? new AnyQuantifier() : new ExistQuantifier();
                    String variable = new String(formulaCharArray, currentIndex + 1, lastVariableNameLength);
                    quantifier.setVariableName(variable);
                    processOperation(quantifier);
                    currentIndex += lastVariableNameLength + 1;

                } else if (formulaCharArray[currentIndex] == '(') {
                    processOperation(new CoveringBracket());
                    currentIndex++;
                } else if (formulaCharArray[currentIndex] == ')') {
                    processOperation(new ClosingBracket());
                    currentIndex++;
                } else if (formulaCharArray[currentIndex] == '&') {
                    processOperation(new Conjunction());
                    currentIndex++;
                } else if (formulaCharArray[currentIndex] == '!') {
                    processOperation(new NegativeOperation());
                    currentIndex++;
                } else if (formulaCharArray[currentIndex] == '@') {
                    processOperation(new Disjunction());
                    currentIndex++;
                } // ||
//            if (formulaCharArray[i] == '>') {} // ->

                else {
                    int predicateNameLength = parseName(formulaCharArray, currentIndex);
                    if (predicateNameLength == -1) {
                        throw new RuntimeException("Мы попытались распарсить имся предиката, но сразу наткнулись на технический символ");
                    }

                    if (formulaCharArray[currentIndex + predicateNameLength] != '(') {
                        throw new RuntimeException("АААА, скобку потеряли, это предикат? что ты мне скормил?");
                    }
                    // названее + скобка
                    int predicateArgumentNameLength = parseName(formulaCharArray, currentIndex + predicateNameLength + 1);

                    if (predicateArgumentNameLength == -1) {
                        throw new RuntimeException("Мы попытались распарсить аргумент предиката, но сразу наткнулись на технический символ");
                    }

                    // Текущее положение + имя предиката + имя аргумента + 2 скобки
                    if (formulaCharArray[currentIndex + predicateNameLength + predicateArgumentNameLength + 1] != ')') {
                        throw new RuntimeException("Проблема с парсингом предиката, не удалось найти закрывающую скобку");
                    }

                    String predicateName = new String(formulaCharArray, currentIndex, predicateNameLength);
                    String argumentName = new String(formulaCharArray, currentIndex + predicateNameLength + 1, predicateArgumentNameLength);
                    Predicate predicate = new Predicate();
                    predicate.setPredicateName(predicateName);
                    // TODO подправить
                    predicate.setArgumentName(argumentName);
                    processAtom(predicate);
                    currentIndex += predicateNameLength + predicateArgumentNameLength + 2;
                }
//                 иначе это какой-то предикат и мы проверяем структуру xxxx(a1, a2,..., an)
            }
            return endParsing();
        }
    }