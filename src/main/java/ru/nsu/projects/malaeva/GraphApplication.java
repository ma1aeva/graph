package ru.nsu.projects.malaeva;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import ru.nsu.projects.malaeva.core.*;
import ru.nsu.projects.malaeva.sdnf.MultipleConjunction;
import ru.nsu.projects.malaeva.sdnf.TreeNode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GraphApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Парсинг формулы
        Parser parser = new ParserImpl();
        Formula formula1 = parser.parseFormula("Ax(f(x) @ f(b) & f(c))");
        Formula formula2 = parser.parseFormula("Ay(f(a) & f(b) @ f(c))");
        Formula formula3 = parser.parseFormula("f(a) @ f(b) @ f(c)");
        Formula formula4 = parser.parseFormula("!f(a) @ f(b) @ !f(c)");

        // Собираем константы
        Set<String> constants = formula1.getConstants();
        constants.addAll(formula2.getConstants());
        Set<String> specialsConstants = Set.of("a1", "a2");
        Formula withoutQuantifiers1 = formula1.withoutQuantifiers(constants, specialsConstants);
        Formula withoutQuantifiers2 = formula2.withoutQuantifiers(constants, specialsConstants);
        Formula withoutQuantifiers3 = formula3.withoutQuantifiers(constants, specialsConstants);
        Formula withoutQuantifiers4 = formula4.withoutQuantifiers(constants, specialsConstants);


        // Составление и заполнение таблиц истинности
        ConfidenceTable confidenceTable1 = new ConfidenceTableImpl();
        ConfidenceTable confidenceTable2 = new ConfidenceTableImpl();
        ConfidenceTable confidenceTable3 = new ConfidenceTableImpl();
        ConfidenceTable confidenceTable4 = new ConfidenceTableImpl();

        confidenceTable1.fillTable(withoutQuantifiers1);
        confidenceTable2.fillTable(withoutQuantifiers2);
        confidenceTable3.fillTable(withoutQuantifiers3);
        confidenceTable4.fillTable(withoutQuantifiers4);

        // Получение сднф
        Set<MultipleConjunction> multipleConjunctionList1 = confidenceTable1.getSdnfConjunctions();
        Set<MultipleConjunction> multipleConjunctionList2 = confidenceTable2.getSdnfConjunctions();
        Set<MultipleConjunction> multipleConjunctionList3 = confidenceTable3.getSdnfConjunctions();
        Set<MultipleConjunction> multipleConjunctionList4 = confidenceTable4.getSdnfConjunctions();


        // Получение мегаформулы (предполагаем, что формулы одной размерности)
        Set<MultipleConjunction> megaformula = confidenceTable1.getAllConjunctions();

        // Этап группирровки по параметрам пропущен

        // Построение дерева
        TreeNode head = new TreeNode(megaformula);
        head.insertInto(new TreeNode(multipleConjunctionList1));
        head.insertInto(new TreeNode(multipleConjunctionList2));
        head.insertInto(new TreeNode(multipleConjunctionList3));
        head.insertInto(new TreeNode(multipleConjunctionList4));


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


        DefaultDirectedGraph<TreeNode, CustomEdge> g = new DefaultDirectedGraph<>(CustomEdge.class);
        head.initGraph(g);


        JGraphXAdapter<TreeNode, CustomEdge> graphAdapter = new JGraphXAdapter<>(g);
        mxIGraphLayout layout = new mxCompactTreeLayout(graphAdapter, false, false);
        graphAdapter.getVertexToCellMap();
        layout.execute(graphAdapter.getDefaultParent());

//        graphAdapter.getStylesheet().setDefaultEdgeStyle(edgeStyle)


//        mxIGraphLayout layout = new FormatEdges.ExtendedCompactTreeLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());


//        Map<String, Object> edgeStyle = graphAdapter.getStylesheet().setDefaultEdgeStyle();
//        System.out.println(edgeStyle);
//        edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);


//        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
//        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 5, Color.WHITE, false, null);
//        System.out.println(mxCellRenderer.createSvgDocument(graphAdapter, null, 2, Color.WHITE, null));
//        org.w3c.dom.Document doc = mxCellRenderer.createSvgDocument(graphAdapter, null, 2, Color.WHITE, null);

//        Image image = SwingFXUtils.toFXImage(capture, null);

        File imgFile = new File("graph.png");
        ImageIO.write(image, "PNG", imgFile);

//        assertTrue(imgFile.exists());



//        Formula testFormula = parser.parseFormula("!f(x) & !g(x)");
//        ConfidenceTable confidenceTable = new ConfidenceTableImpl();
//        confidenceTable.fillTable(testFormula);
//        System.out.println(confidenceTable.getSdnfConjunctions());





        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/mainView.fxml")));
        primaryStage.setTitle("MVC Example App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}