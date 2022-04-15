package ru.nsu.projects.malaeva;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.view.mxStylesheet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Setter;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import ru.nsu.projects.malaeva.core.StandardParser;
import ru.nsu.projects.malaeva.sdnf.TreeNode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class MainController {

    @FXML
    public Pane pane;
    public VBox vbox;

    @Setter
    private Stage stage;

    @FXML
    public void addNewRow() {
        TextField formulaTextField = new TextField();
        formulaTextField.setPromptText("formula");

        TextField probabilityTextField = new TextField();
        probabilityTextField.setPromptText("pr.");

        Label counterLabel = new Label((vbox.getChildren().size() + 1) + ".");
        counterLabel.setPrefWidth(30);

        probabilityTextField.setPrefWidth(50);
        formulaTextField.setPrefWidth(500);

        HBox hbox = new HBox();
        hbox.setSpacing(5);

        Button removeRowButton = new Button();

        removeRowButton.setText("-");
        removeRowButton.setOnAction(
                action -> {
                    if (vbox.getChildren().size() > 1) {
                        vbox.getChildren().remove(((Button) action.getSource()).getParent());

                        setRowsNumeration();
                        setRemoveRowButtonsColors();

                        setCorrectWindowSize();
                    }
                }
        );

        hbox.getChildren().addAll(counterLabel, formulaTextField, probabilityTextField, removeRowButton);
        vbox.getChildren().add(hbox);

        setRemoveRowButtonsColors();
        setCorrectWindowSize();
    }

    private void setCorrectWindowSize() {
        if (stage != null) {
            stage.sizeToScene();
        }
    }

    private void setRemoveRowButtonsColors() throws ClassCastException{
        if (vbox.getChildren().size() == 1) {
            ((HBox) vbox.getChildren().get(0)).getChildren().get(3).setStyle("-fx-background-color: grey");
        }
        else if (vbox.getChildren().size() > 1) {
            vbox.getChildren().stream()
                    .map(node -> (Button)((HBox)node).getChildren().get(3))
                    .forEach(removeButton -> removeButton.setStyle("-fx-background-color: #e75e5e;"));
        }
    }

    private void setRowsNumeration() throws ClassCastException{
        final int[] number = {1};
        vbox.getChildren().stream()
                .map(node -> (Label)((HBox)node).getChildren().get(0))
                .forEach(label -> {
                    label.setText(number[0] + ".");
                    number[0]++;
                });
    }

    @FXML public void addAnyQuantifier() {
        addTextToLastActiveField("∀");
    }

    @FXML public void addExistQuantifier() {
        addTextToLastActiveField("∃");
    }

    @FXML public void addDisjunction() {
        addTextToLastActiveField("v");
    }

    @FXML public void addConjunction() {
        addTextToLastActiveField("∧");
    }

    @FXML public void addImplication() {
        addTextToLastActiveField("→");
    }

    @FXML
    public void buildGraph() throws IOException {
        List<FormulaDTO> formulaDTOs = prepareFormulaDtos();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null){
            //No Directory selected
        } else {
            System.out.println(selectedDirectory.getAbsolutePath());

            new Thread(() -> {
                GraphBuilder graphBuilder = new GraphBuilder();

                Map<Map<String, Set<String>>, Graph<TreeNode, CustomEdge>> graphMap =
                        graphBuilder.buildGraph(new StandardParser(), formulaDTOs, Set.of("a1", "a2"));

                int counter = 0;
                for (Graph<TreeNode, CustomEdge> graph : graphMap.values()) {
                    JGraphXAdapter<TreeNode, CustomEdge> concreteGraphAdapter = new JGraphXAdapter<>(graph);
                    mxIGraphLayout layout = new mxCompactTreeLayout(concreteGraphAdapter, false, false);
                    layout.execute(concreteGraphAdapter.getDefaultParent());

                    concreteGraphAdapter.getEdgeToCellMap().forEach(
                            (customEdge, mxICell) -> {
                                String targetStyle = "";
                                targetStyle = switch (customEdge.getRelation()) {
                                    case CORRECT -> mxStyleUtils.setStyle(targetStyle, mxConstants.STYLE_STROKECOLOR, "green");
                                    case INCORRECT -> mxStyleUtils.setStyle(targetStyle, mxConstants.STYLE_STROKECOLOR, "red");
                                };
                                mxICell.setStyle(targetStyle);
                            }
                    );

//                    concreteGraphAdapter.setStylesheet((new mxStylesheet()).setDefaultEdgeStyle(Map.of(mxConstants.STYLE_FLLCOLOR, "red")));

                    BufferedImage image = mxCellRenderer.createBufferedImage(concreteGraphAdapter, null, 5,
                            Color.BLACK, false, null);
                    File file = new File(selectedDirectory.getAbsolutePath() + "/" + "graph_" + counter + ".png");

                    try {
                        ImageIO.write(image, "PNG", file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    counter++;


                }
            }).start();

        }
    }

    public List<FormulaDTO> prepareFormulaDtos() {
        return vbox.getChildren().stream()
                .map(node -> {
                    TextField formula = (TextField) ((HBox)node).getChildren().get(1);
                    TextField probability = (TextField) ((HBox)node).getChildren().get(2);

                    if (formula.getText().isBlank()) {
                        throw new RuntimeException("Есть пустые поля без формул.");
                    }
                    try {
                        double probabilityValue = Double.parseDouble(probability.getText());
                        return FormulaDTO.builder()
                                .probability(probabilityValue)
                                .formula(formula.getText())
                                .build();
                    } catch (NumberFormatException numberFormatException) {
                        throw new RuntimeException("Неверный формат вероятности.");
                    }
                }).collect(Collectors.toList());
    }

    @FXML
    public void initialize() {
        addNewRow();
        vbox.setSpacing(7);
    }

    private void addTextToLastActiveField(String text) {
        vbox.getChildren().stream()
                .map(node -> (TextField)((HBox)node).getChildren().get(1))
                .forEach(formulaField -> {
                    if (formulaField.isFocused()) {
                        String before = formulaField.getText().substring(0, formulaField.getCaretPosition());
                        String after = formulaField.getText().substring(formulaField.getCaretPosition());
                        formulaField.setText(before + text + after);
                        formulaField.positionCaret(before.length() + text.length());
                    }
                });
    }
}