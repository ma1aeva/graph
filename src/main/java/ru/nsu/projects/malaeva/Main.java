package ru.nsu.projects.malaeva;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import ru.nsu.projects.malaeva.core.*;
import ru.nsu.projects.malaeva.sdnf.MultipleConjunction;
import ru.nsu.projects.malaeva.sdnf.TreeNode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        // Парсинг формулы
        Parser parser = new ParserImpl();
        Formula formula1 = parser.parseFormula("Ax(f(x))");
        Formula formula2 = parser.parseFormula("Ax(f(x))");

        // Собираем константы
        Set<String> constants = formula1.getConstants();
        constants.addAll(formula2.getConstants());
        Set<String> specialsConstants = Set.of("a1", "a2");
        Formula withoutQuantifiers1 = formula1.withoutQuantifiers(constants, specialsConstants);
        Formula withoutQuantifiers2 = formula2.withoutQuantifiers(constants, specialsConstants);


        // Составление и заполнение таблиц истинности
        ConfidenceTable confidenceTable1 = new ConfidenceTableImpl();
        ConfidenceTable confidenceTable2 = new ConfidenceTableImpl();

        confidenceTable1.fillTable(withoutQuantifiers1);
        confidenceTable2.fillTable(withoutQuantifiers2);

        System.out.println(confidenceTable1.getPredicates());
        System.out.println(confidenceTable2.getPredicates());

        // Получение сднф
        Set<MultipleConjunction> multipleConjunctionList1 = confidenceTable1.getSdnfConjunctions();
        Set<MultipleConjunction> multipleConjunctionList2 = confidenceTable2.getSdnfConjunctions();

        // Получение мегаформулы (предполагаем, что формулы одной размерности)
        Set<MultipleConjunction> megaformula = confidenceTable1.getAllConjunctions();

        // Этап группирровки по параметрам пропущен

        // Построение дерева
        TreeNode head = new TreeNode(megaformula);
        head.insertInto(new TreeNode(multipleConjunctionList1));
        head.insertInto(new TreeNode(multipleConjunctionList2));


        megaformula.stream()
                        .map(multipleConjunction -> new TreeNode(Set.of(multipleConjunction)))
                                .forEach(head::insertInto);

//        System.out.println("[1] >= [2]: " + multipleConjunctionList1.containsAll(multipleConjunctionList2));
//        System.out.println("[1] <= [2]: " + multipleConjunctionList2.containsAll(multipleConjunctionList1));
//        System.out.println("[3] >= [1]: " + fullFormula.containsAll(multipleConjunctionList1));
//        System.out.println("[3] >= [1]: " + fullFormula.containsAll(multipleConjunctionList2));
//
//         Сравнение таблиц истинности
//        System.out.println(confidenceTable1.getPredicates());
//        System.out.println(confidenceTable2.getPredicates());
//        System.out.println(confidenceTable1.getPredicates().equals(confidenceTable2.getPredicates()));


        DefaultDirectedGraph<TreeNode, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

//        g1.addVertex(head);


//        String x1 = "x1lol";
//        String x2 = "x2";
//        String x3 = "x3";
//
//        g.addVertex(x1);
//        g.addVertex(x2);
//        g.addVertex(x3);
//
//        g.addEdge(x1, x2);
//        g.addEdge(x2, x3);
//        g.addEdge(x3, x1);

        head.initGraph(g);

        JGraphXAdapter<TreeNode, DefaultEdge> graphAdapter = new JGraphXAdapter<>(g);

//        graphAdapter.getStylesheet().setDefaultEdgeStyle(edgeStyle)

//        graphAdapter.getStylesheet().setDefaultEdgeStyle();

//        mxIGraphLayout layout = new FormatEdges.ExtendedCompactTreeLayout(graphAdapter);
//        layout.execute(graphAdapter.getDefaultParent());

        mxIGraphLayout layout = new mxCompactTreeLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

//        Map<String, Object> edgeStyle = graphAdapter.getStylesheet().setDefaultEdgeStyle();
//        System.out.println(edgeStyle);
//        edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);


//        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
//        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 1, Color.WHITE, false, null);
//        System.out.println(mxCellRenderer.createSvgDocument(graphAdapter, null, 2, Color.WHITE, null));
//        org.w3c.dom.Document doc = mxCellRenderer.createSvgDocument(graphAdapter, null, 2, Color.WHITE, null);

        File imgFile = new File("graph.png");
        ImageIO.write(image, "PNG", imgFile);

//        assertTrue(imgFile.exists());



//        Formula testFormula = parser.parseFormula("!f(x) & !g(x)");
//        ConfidenceTable confidenceTable = new ConfidenceTableImpl();
//        confidenceTable.fillTable(testFormula);
//        System.out.println(confidenceTable.getSdnfConjunctions());
    }
}