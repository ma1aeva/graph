package ru.nsu.projects.malaeva.sdnf;

import lombok.RequiredArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TreeNode {
    private final Set<MultipleConjunction> multipleConjunctionSet;
    private List<TreeNode> parents = new ArrayList<>();
    private List<TreeNode> children = new ArrayList<>();


    public boolean insertInto(TreeNode newNode) {
        if (isSubformula(newNode.multipleConjunctionSet)) {
            boolean deep = false;
            for (TreeNode child : children) {
                if (child.insertInto(newNode))
                    deep = true;
            }
            if (!deep) {
                children.add(newNode);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return multipleConjunctionSet.stream()
                .map(Objects::toString)
                .map(conjunction -> "(" + conjunction + ")")
                .collect(Collectors.joining(" or "));
    }

    private void initVertex(Graph<TreeNode, DefaultEdge> graph) {
        graph.addVertex(this);
        for (TreeNode child : children) {
            child.initVertex(graph);
        }
    }

    private void addEdges(Graph<TreeNode, DefaultEdge> graph) {
        for (TreeNode child : children) {
            graph.addEdge(this, child);
            child.addEdges(graph);
        }
    }

    public void initGraph(Graph<TreeNode, DefaultEdge> graph) {
        initVertex(graph);
        addEdges(graph);
    }

    private boolean isSubformula(Set<MultipleConjunction> newNodeMultipleConjunction) {
        return multipleConjunctionSet.containsAll(newNodeMultipleConjunction);
    }
}