package ru.nsu.projects.malaeva;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import ru.nsu.projects.malaeva.core.ConfidenceTable;
import ru.nsu.projects.malaeva.core.ConfidenceTableImpl;
import ru.nsu.projects.malaeva.core.Formula;
import ru.nsu.projects.malaeva.core.Parser;
import ru.nsu.projects.malaeva.sdnf.MultipleConjunction;
import ru.nsu.projects.malaeva.sdnf.TreeNode;

import java.util.*;
import java.util.stream.Collectors;

public class GraphBuilder {
    public Map<Map<String, Set<String>>, Graph<TreeNode, CustomEdge>> buildGraph(Parser parser, List<FormulaDTO> formulaDTOList,
                                                  Set<String> specialsConstants) {

        HashMap<Formula, FormulaDTO> parsedFormulas = new HashMap<>();

        // Парсим все формулы, которые мы получили на вход, заполняем мэпу
        for (FormulaDTO formulaDTO : formulaDTOList) {
            parsedFormulas.put(parser.parseFormula(formulaDTO.getFormula()), formulaDTO);
        }

        // Получаем все константы из всех формул
        Set<String> constants = parsedFormulas.keySet().stream()
                .map(Formula::getConstants)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        Map<Formula, FormulaDTO> withoutQuantifiersFormulaMap = new HashMap<>();

        // Избавляемся от кванторов
        for (Map.Entry<Formula, FormulaDTO> formulaEntry : parsedFormulas.entrySet()) {
            Formula withoutQuantifiers = formulaEntry.getKey().withoutQuantifiers(constants, specialsConstants);
            withoutQuantifiersFormulaMap.put(withoutQuantifiers, formulaEntry.getValue());
        }

        Map<Formula, ConfidenceTable> confidenceTableMap = new HashMap<>();
        Map<Formula, Set<MultipleConjunction>> sdnfMap = new HashMap<>();

        // Нахождение таблиц истинности, получение сднф
        for (Formula formula : withoutQuantifiersFormulaMap.keySet()) {
            ConfidenceTable confidenceTable = new ConfidenceTableImpl();
            confidenceTable.fillTable(formula);
            confidenceTableMap.put(formula, confidenceTable);

            sdnfMap.put(formula, confidenceTable.getSdnfConjunctions());
        }

        Map<Map<String, Set<String>>, Set<Formula>> sortedFormulas = new HashMap<>();


        // Процесс сортировки
        for (Formula formula : withoutQuantifiersFormulaMap.keySet()) {
            Map<String, Set<String>> atoms = new HashMap<>();
            formula.fillAtoms(atoms);

            sortedFormulas.computeIfAbsent(atoms, k -> new HashSet<>());
            sortedFormulas.get(atoms).add(formula);
        }

        Map<Map<String, Set<String>>, Graph<TreeNode, CustomEdge>> heads = new HashMap<>();


        for (Map.Entry<Map<String, Set<String>>, Set<Formula>> sortedFormulaEntry : sortedFormulas.entrySet()) {
            Graph<TreeNode, CustomEdge> graph = new DefaultDirectedGraph<>(CustomEdge.class);

            Formula representative = sortedFormulaEntry.getValue().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Проблема с сортировкой формул"));

            // Сформировали получили большую формулу, держим ее в качестве головы
            Set<MultipleConjunction> allConjunctions = confidenceTableMap.get(representative).getAllConjunctions();
            TreeNode head = new TreeNode(allConjunctions);
            head.setProbability(1.0);
            head.setAccurate(true);

            // Теперь создаем ноды для промежуточных формул
            for (Formula formula : sortedFormulas.get(sortedFormulaEntry.getKey())) {
                Set<MultipleConjunction> sdnf = sdnfMap.get(formula);
                TreeNode newNode = new TreeNode(sdnf);
                newNode.setFormulaDTO(withoutQuantifiersFormulaMap.get(formula));
                head.insertInto(newNode);
            }

            // Загрузка самых базовых нод
            allConjunctions.stream()
                .map(multipleConjunction -> new TreeNode(Set.of(multipleConjunction)))
                .forEach(head::insertInto);

            // Инициализируем граф
            head.setProbabilityIfPossible();
            head.weight();
            head.initGraph(graph);
            heads.put(sortedFormulaEntry.getKey(), graph);
        }

        return heads;
    }
}