package org.dnu.samoylov.controller.speach;

import com.sun.jnlp.ApiDialog;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.dnu.samoylov.controller.main.AbstractFxmlController;
import org.dnu.samoylov.service.DinamicProgramming;
import org.dnu.samoylov.service.LineRepresentation;
import org.dnu.samoylov.service.OpenHelper;
import org.dnu.samoylov.service.Sound;
import org.dnu.samoylov.service.matrix.FourerMatrix;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Controller
public class MainController extends AbstractFxmlController implements Initializable {


    public LineChart<Number, Number> chart1;
    public LineChart<Number, Number> chart2;
    public LineChart<Number, Number> chart3;
    public LineChart<Number, Number> chart4;
    public LineChart<Number, Number> chart5;
    public TextField textBox1;
    public Slider trackBar1;
    public Slider trackBar2;
    public RadioMenuItem YolshSelected;

    public TableView<Dto> table;
    public TableColumn nameCol;
    public TableColumn distanceCol;
    public TableColumn minCol;
    private Path path;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chart1.setCreateSymbols(false);
        chart2.setCreateSymbols(false);
        chart3.setCreateSymbols(false);
        chart4.setCreateSymbols(false);
        chart5.setCreateSymbols(false);

        trackBar1.setBlockIncrement(1);
        trackBar2.setBlockIncrement(1);

        trackBar1.valueProperty().addListener((obs, oldVal, newVal) -> {
            int value = newVal.intValue();
            trackBar1.setValue(value);

            if (YolshSelected.isSelected()) {
                DrawWalschTransform((int) trackBar1.getValue());
            } else {
                DrawFourierTransform((int) trackBar1.getValue());
            }
        });

        trackBar2.valueProperty().addListener((obs, oldVal, newVal) -> {
            int value = newVal.intValue();
            trackBar2.setValue(value);

            if (YolshSelected.isSelected()) {
                DrawWalsch((int) trackBar2.getValue());
                DrawWalschSmoothing((int) trackBar2.getValue());
            } else {
                DrawFourer((int) trackBar2.getValue());
                DrawFourerSmoothing((int) trackBar2.getValue());
            }
        });


        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        distanceCol.setCellValueFactory(new PropertyValueFactory("distance"));
        minCol.setCellValueFactory(new PropertyValueFactory("min"));
    }


    @FXML
    public void loadData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data/"));
        this.path = fileChooser.showOpenDialog(getView().getScene().getWindow()).toPath();
        Path path = this.path;
        Sound dataSet = loadDataSet(path);
        twerkWithDataSet(dataSet);
    }


    Sound sound;

    protected void twerkWithDataSet(Sound dataSet) {
        sound = dataSet;


        trackBar1.setMin(0);
        trackBar1.setMax(sound.countOfLines - 1);

        trackBar2.setMin(0);
        trackBar2.setMax(LineRepresentation.lineCount - 1);


        if (YolshSelected.isSelected()) {
            DrawWalschTransform((int) trackBar1.getValue());
        } else {
            DrawFourierTransform((int) trackBar1.getValue());
        }

        if (YolshSelected.isSelected()) {
            DrawWalsch((int) trackBar2.getValue());
            DrawWalschSmoothing((int) trackBar2.getValue());
            OpenHelper.saveToFileArray(sound, "walsch", path.toString()
                    .replace(".WAV", ".dat"));
        } else {
            DrawFourer((int) trackBar2.getValue());
            DrawFourerSmoothing((int) trackBar2.getValue());
            OpenHelper.saveToFileArray(sound, "fourer", path.toString()
                    .replace(".WAV", ".dat"));
        }


        DrawChart(sound);
        DrawChartWhithoutLatentPeriods(sound);
        DrawNormalizationChart(sound);

    }


    private Sound loadDataSet(final Path filePath) {
        try {
            byte[] bytes = Files.readAllBytes(filePath);

            return OpenHelper.open(bytes, Double.parseDouble(textBox1.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    void DrawChart(Sound sound) {
        LineChart<Number, Number> chart = chart1;
        chart.getData().clear();
        chart.layout();

        ((NumberAxis) chart.getXAxis()).setLowerBound(0);
        ((NumberAxis) chart.getYAxis()).setLowerBound(sound.arr.stream().min(Comparator.<Short>naturalOrder()).get());
        ((NumberAxis) chart.getYAxis()).setUpperBound(sound.arr.stream().max(Comparator.<Short>naturalOrder()).get());

        final XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for (int i = 0; i < sound.arr.size(); i++) {
            XYChart.Data<Number, Number> xyData = new XYChart.Data<>(i, sound.arr.get(i));
            series.getData().add(xyData);
        }

        chart.getData().add(series);
    }

    void DrawChartWhithoutLatentPeriods(Sound sound) {
        privateDrawChart(chart2, sound.arrWhithoutLatentPeriods);
    }

    void DrawNormalizationChart(Sound sound) {
        privateDrawChart(chart3, sound.normalizationArr);
    }


    public void DrawFourer(int l) {
        privateDrawChart(chart5, sound.lineRepresentationFourier, l, sound.countOfLines, true, "Спектр");
    }

    public void DrawWalsch(int l) {
        privateDrawChart(chart5, sound.lineRepresentationWalsch, l, sound.countOfLines, true, "Спектр");
    }

    public void DrawFourerSmoothing(int l) {
        privateDrawChart(chart5, sound.lineRepresentationFourierSmoothing, l, sound.countOfLines, false, "ФНЧ");
    }

    public void DrawWalschSmoothing(int l) {
        privateDrawChart(chart5, sound.lineRepresentationWalschSmoothing, l, sound.countOfLines, false, "ФНЧ");
    }

    public void DrawWalschTransform(int l) {
        privateDrawChart(chart4, sound.laneRepresentationWalsch, l, 256);
    }

    public void DrawFourierTransform(int l) {
        privateDrawChart(chart4, sound.laneRepresentationFourier, l, 256 / 2);
    }


    private void privateDrawChart(LineChart<Number, Number> chart, List<? extends Number> list) {
        chart.getData().clear();
        chart.layout();

        final XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for (int i = 0; i < list.size(); i++) {
            XYChart.Data<Number, Number> xyData = new XYChart.Data<>(i, list.get(i));
            series.getData().add(xyData);
        }

        chart.getData().add(series);
    }


    private void privateDrawChart(LineChart<Number, Number> chart, double[][] list, int l, int xUpperBound) {
        privateDrawChart(chart, list, l, xUpperBound, true, "");
    }

    private void privateDrawChart(LineChart<Number, Number> chart, double[][] list, int l, int xUpperBound, boolean needClear, String name) {
        if (needClear) {
            chart.getData().clear();
            chart.layout();
        }

        final XYChart.Series<Number, Number> series = new XYChart.Series<>();

        ((NumberAxis) chart.getYAxis()).setLowerBound(0);
        ((NumberAxis) chart.getXAxis()).setLowerBound(0);
        ((NumberAxis) chart.getXAxis()).setUpperBound(xUpperBound - 1);

        for (int i = 0; i < xUpperBound; i++) {
            XYChart.Data<Number, Number> xyData = new XYChart.Data<>(i, list[l][i]);
            series.getData().add(xyData);
        }
        series.setName(name);
        chart.getData().add(series);
    }


    public void downloadEtalons() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("dat files (*.dat)", "*.dat"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data/"));
        List<File> fileList = fileChooser.showOpenMultipleDialog(getView().getScene().getWindow());
            List<FourerMatrix> matrix = new ArrayList<>();

        for (File file : fileList) {

                List<String> lines = Files.readAllLines(file.toPath());
            String[] elem = lines.get(0).split(" ");

                double[][] array = new double[lines.size()][elem.length];
                for (int i = 0; i < lines.size(); i++) {
                    elem = lines.get(i).split(" ");

                    for (int j = 0; j < elem.length; j++) {
                        array[i][j]= Double.valueOf(elem[j]
                                .replace(',', '.'));
                    }

                }

                matrix.add(new FourerMatrix(array, file.getName()));
            }

            DinamicProgramming dinamic;
            if (!YolshSelected.isSelected()) {
                dinamic = new DinamicProgramming(matrix, sound.lineRepresentationFourierSmoothing);
            } else {
                dinamic = new DinamicProgramming(matrix, sound.lineRepresentationWalschSmoothing);
            }
            dinamic.CalculateAllMatrixDistance();
            dinamic.InitDistances();
            DGVDistances(dinamic.distances, matrix);

    }


    private void DGVDistances(List<Double> listOfDistances, List<FourerMatrix> fourer) {
        ObservableList<Dto> items = table.getItems();
        items.clear();

        Double min = listOfDistances.stream().min(Comparator.<Double>naturalOrder()).get();
        for (int i = 0; i < listOfDistances.size(); i++) {
            String isMin = "";
            if (Objects.equals(listOfDistances.get(i), min)) {
                isMin = "x";
            }

            items.add(new Dto(
                    String.valueOf(i + 1) + " " + fourer.get(i).name,
                    String.valueOf(listOfDistances.get(i)),
                    isMin));

        }
    }


private static final class Dto {
    public String nameCol;
    public String distanceCol;
    public String minCol;

    public Dto(String nameCol, String distanceCol, String minCol) {
        this.nameCol = nameCol;
        this.distanceCol = distanceCol;
        this.minCol = minCol;
    }

    public String getNameCol() {
        return nameCol;
    }

    public void setNameCol(String nameCol) {
        this.nameCol = nameCol;
    }

    public String getDistanceCol() {
        return distanceCol;
    }

    public void setDistanceCol(String distanceCol) {
        this.distanceCol = distanceCol;
    }

    public String getMinCol() {
        return minCol;
    }

    public void setMinCol(String minCol) {
        this.minCol = minCol;
    }
}
}
