package ru.nsu.projects.malaeva;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GraphApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Парсинг формулы
//        Parser parser = new ParserImpl();
//        Formula formula1 = parser.parseFormula("Ax(f(x) @ f(b) & f(c))");
//        Formula formula2 = parser.parseFormula("Ay(f(a) & f(b) @ f(c))");
//        Formula formula3 = parser.parseFormula("f(a) @ f(b) @ f(c)");
//        Formula formula4 = parser.parseFormula("!f(a) @ f(b) @ !f(c)");

        // Собираем константы
//        Set<String> constants = formula1.getConstants();
//        constants.addAll(formula2.getConstants());
//        Set<String> specialsConstants = Set.of("a1", "a2");
//        Formula withoutQuantifiers1 = formula1.withoutQuantifiers(constants, specialsConstants);
//        Formula withoutQuantifiers2 = formula2.withoutQuantifiers(constants, specialsConstants);
//        Formula withoutQuantifiers3 = formula3.withoutQuantifiers(constants, specialsConstants);
//        Formula withoutQuantifiers4 = formula4.withoutQuantifiers(constants, specialsConstants);


        // Составление и заполнение таблиц истинности
//        ConfidenceTable confidenceTable1 = new ConfidenceTableImpl();
//        ConfidenceTable confidenceTable2 = new ConfidenceTableImpl();
//        ConfidenceTable confidenceTable3 = new ConfidenceTableImpl();
//        ConfidenceTable confidenceTable4 = new ConfidenceTableImpl();
//
//        confidenceTable1.fillTable(withoutQuantifiers1);
//        confidenceTable2.fillTable(withoutQuantifiers2);
//        confidenceTable3.fillTable(withoutQuantifiers3);
//        confidenceTable4.fillTable(withoutQuantifiers4);

        // Получение сднф
//        Set<MultipleConjunction> multipleConjunctionList1 = confidenceTable1.getSdnfConjunctions();
//        Set<MultipleConjunction> multipleConjunctionList2 = confidenceTable2.getSdnfConjunctions();
//        Set<MultipleConjunction> multipleConjunctionList3 = confidenceTable3.getSdnfConjunctions();
//        Set<MultipleConjunction> multipleConjunctionList4 = confidenceTable4.getSdnfConjunctions();


        // Получение мегаформулы (предполагаем, что формулы одной размерности)
//        Set<MultipleConjunction> megaformula = confidenceTable1.getAllConjunctions();

        // Этап группирровки по параметрам пропущен

        // Построение дерева
//        TreeNode head = new TreeNode(megaformula);
//        head.insertInto(new TreeNode(multipleConjunctionList1));
//        head.insertInto(new TreeNode(multipleConjunctionList2));
//        head.insertInto(new TreeNode(multipleConjunctionList3));
//        head.insertInto(new TreeNode(multipleConjunctionList4));


//        megaformula.stream()
//                .map(multipleConjunction -> new TreeNode(Set.of(multipleConjunction)))
//                .forEach(head::insertInto);



//        DefaultDirectedGraph<TreeNode, CustomEdge> g = new DefaultDirectedGraph<>(CustomEdge.class);
//        head.initGraph(g);


//        JGraphXAdapter<TreeNode, CustomEdge> graphAdapter = new JGraphXAdapter<>(g);
//        mxIGraphLayout layout = new mxCompactTreeLayout(graphAdapter, false, false);
//        graphAdapter.getVertexToCellMap();
//        layout.execute(graphAdapter.getDefaultParent());



//        layout.execute(graphAdapter.getDefaultParent());



//        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 5, Color.WHITE, false, null);


//        File imgFile = new File("graph.png");
//        ImageIO.write(image, "PNG", imgFile);



//        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> showErrorDialog(t, e)));
//        Thread.currentThread().setUncaughtExceptionHandler(this::showErrorDialog);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainView.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Graph Builder");

        MainController controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

//    private void showErrorDialog(Thread t, Throwable e) {
//        Dialogs.create().title("Error")
//                .message("An uncaught exception was thrown in thread " + t
//                        + ". Click below to view the stacktrace, or close this "
//                        + "dialog to terminate the application.")
//                .style(DialogStyle.NATIVE)
//                .showExceptionInNewWindow(e);
//        Platform.exit();
//    }

    public static void buildGraph(List<FormulaDTO> formulaDTOList, File saveTo) {
        Set<String> specialConstant = Set.of("a1", "a2");
    }

    @Override
    public void stop() throws Exception {
        Platform.exit();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}