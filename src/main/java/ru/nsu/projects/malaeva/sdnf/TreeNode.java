package ru.nsu.projects.malaeva.sdnf;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jgrapht.Graph;
import ru.nsu.projects.malaeva.CustomEdge;
import ru.nsu.projects.malaeva.FormulaDTO;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TreeNode {

    @Getter @Setter
    private Double probability;

    @Getter @Setter
    private boolean isAccurate;

    @Getter @Setter
    private FormulaDTO formulaDTO;

    private final Set<MultipleConjunction> multipleConjunctionSet;
    private final Set<TreeNode> children = new HashSet<>();

    public void weight() {
        double childrenProbabilitySum = children.stream()
                .filter(node -> node.getProbability() != null)
                .mapToDouble(TreeNode::getProbability)
                .sum();
        double remainder = probability - childrenProbabilitySum;

        List<TreeNode> childrenWithoutProbability = children.stream()
                .filter(node -> node.getProbability() == null)
                .toList();

        // Если у нас один потомок с неопределенной вероятностью, то задаем ее как точную
        if (childrenWithoutProbability.size() == 1) {
            TreeNode singleChildWithoutProbability = childrenWithoutProbability.get(0);
            singleChildWithoutProbability.setProbability(remainder);
            singleChildWithoutProbability.setAccurate(true);
        }
        else {
            for (TreeNode child : childrenWithoutProbability) {
                child.setProbability(remainder);
                child.setAccurate(false);
            }
        }
        // Проводим операцию взвешивания для потомков
        for (TreeNode child : children) {
            child.weight();
        }
    }

    public void setProbabilityIfPossible() {
        if (probability == null && formulaDTO != null) {
            System.out.println(formulaDTO);
            System.out.println(formulaDTO.getProbability());
            probability = formulaDTO.getProbability();
            isAccurate = true;
        }
        children.forEach(TreeNode::setProbabilityIfPossible);
    }

    public void insertInto(TreeNode newNode) {

        if (equals(newNode)) {
            return;
        }

        // Формируем сет нод из детей, которые станут потомками переданной вставляемой ноды
        Set<TreeNode> newNodeChildrenSet = children.stream()
                .filter(children -> newNode.isSubformula(children) && !newNode.equals(children))
                .collect(Collectors.toSet());

        // Формируем сет нод, которые являются родительскими по отношению к вставляемой ноде
        // В том числе здесь может быть точно такая же нода, поэтому есть проверка вначале
        Set<TreeNode> superFormulas = children.stream()
                .filter(children -> children.isSubformula(newNode))
                .collect(Collectors.toSet());

        // Если в наших потомках нет родителя для всьавляемой ноды, то просто добавляем ее в свои потомки
        if (superFormulas.isEmpty()) {
            children.add(newNode);
        } else {
            superFormulas.forEach(superFormula -> superFormula.insertInto(newNode));
        }
        newNodeChildrenSet.forEach(newNode::insertInto);
        children.removeAll(newNodeChildrenSet);
    }

    @Override
    public String toString() {
        String formulaString = multipleConjunctionSet.stream()
                .map(Objects::toString)
                .map(conjunction -> "(" + conjunction + ")")
                .collect(Collectors.joining(" v "));
        if (probability != null) {
            String probabilityString;
            if (isAccurate) {
                probabilityString = " (" + probability + ")";
            }
            else {
                probabilityString = (probability <= 0) ? " [" + String.format("%1$,.2f", probability) + "; " + 0 + "]" :
                        " [" + 0 + "; " + String.format("%1$,.2f", probability) + "]";
            }
            formulaString += probabilityString;
        }
        return formulaString;
    }


    private void initVertex(Graph<TreeNode, CustomEdge> graph) {
        graph.addVertex(this);
        for (TreeNode child : children) {
            child.initVertex(graph);
        }
    }

    private void addEdges(Graph<TreeNode, CustomEdge> graph) {
        for (TreeNode child : children) {
            if (child.getProbability() != null && this.getProbability() != null) {
                CustomEdge.ProbabilityRelation relation = (this.getProbability() < child.getProbability() && (
                        this.isAccurate() && child.isAccurate)) ?
                        CustomEdge.ProbabilityRelation.INCORRECT : CustomEdge.ProbabilityRelation.CORRECT;
                graph.addEdge(this, child, new CustomEdge(relation));
                child.addEdges(graph);
            }
        }
    }

    public void initGraph(Graph<TreeNode, CustomEdge> graph) {
        initVertex(graph);
        addEdges(graph);
    }

    private boolean isSubformula(TreeNode treeNode) {
        return multipleConjunctionSet.containsAll(treeNode.multipleConjunctionSet);
    }

    private boolean equals(TreeNode treeNode) {
        return multipleConjunctionSet.equals(treeNode.multipleConjunctionSet);
    }
}