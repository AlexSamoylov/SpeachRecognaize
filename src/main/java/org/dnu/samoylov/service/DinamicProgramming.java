package org.dnu.samoylov.service;

import org.dnu.samoylov.service.matrix.DistanceMatrix;
import org.dnu.samoylov.service.matrix.FourerMatrix;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DinamicProgramming {

    List<FourerMatrix> fileMatrix;
    List<DistanceMatrix> distanceMatrixes;
    FourerMatrix currentMatrix;
    public List<Double> distances;

    public DinamicProgramming(List<FourerMatrix> fileMatrix, double[][] currentMatrix) {
        this.fileMatrix = fileMatrix;
        this.currentMatrix = new FourerMatrix(currentMatrix, "");
        this.distanceMatrixes = new ArrayList<>();
        this.distances = new ArrayList<>();
    }



    public void CalculateAllMatrixDistance() {
        for (int i = 0; i < fileMatrix.size(); i++) {
            distanceMatrixes.add(InitDistanceMatrix(currentMatrix, fileMatrix.get(i)));
        }
    }

    public void InitDistances() {
        for (int i = 0; i < distanceMatrixes.size(); i++) {
            distances.add(CalculateMinDistance(distanceMatrixes.get(i)));
        }
    }





    private Double[] GetColum(double[][] array, int columNumber) {
        List<Double> listElem = new ArrayList<>();
        for (int i = 0; i < array[0].length; i++) {
            listElem.add(array[i][columNumber]);
        }
        return listElem.toArray(new Double[listElem.size()]);
    }

    private double CalculateEuclideanMetric(Double[] firstArray, Double[] secondArray) {
        double distance = 0;

        for (int i = 0; i < firstArray.length; i++) {
            distance += Math.pow(secondArray[i] - firstArray[i], 2);
        }
        distance = Math.sqrt(distance);

        return distance;
    }

    private DistanceMatrix InitDistanceMatrix(FourerMatrix firstMatrix, FourerMatrix secondMatrix) {
        Double[][] distanceMatrix = new Double[firstMatrix.array[1].length][secondMatrix.array[1].length];
        for (int i = 0; i < firstMatrix.array[1].length; i++) {
            for (int j = 0; j < secondMatrix.array[1].length; j++) {
                distanceMatrix[i][j] = CalculateEuclideanMetric(GetColum(firstMatrix.array, i), GetColum(secondMatrix.array, j));
            }
        }
        return new DistanceMatrix(distanceMatrix);
    }

    private double CalculateMinDistance(DistanceMatrix matrix) {
        double[][] d = new double[matrix.array[0].length][matrix.array[1].length];
        double[][] count = new double[matrix.array[0].length][matrix.array[1].length];

        for (int i = 0; i < matrix.array[0].length; i++) {
            for (int j = 0; j < matrix.array[1].length; j++) {
                List<Double> list = new ArrayList<>();
                List<Double> listCount = new ArrayList<>();
                if ((i == 0) && (j == 0)) {
                    list.add(0d);
                    listCount.add(0d);
                } else if (i == 0) {
                    list.add(d[i][j - 1]);
                    listCount.add(count[i][j - 1]);
                } else if (j == 0) {
                    list.add(d[i - 1][j]);
                    listCount.add(count[i - 1][j]);
                } else {
                    list.add(d[i - 1][j]);
                    list.add(d[i - 1][j - 1]);
                    list.add(d[i][j - 1]);

                    listCount.add(count[i - 1][j]);
                    listCount.add(count[i - 1][j - 1]);
                    listCount.add(count[i][j - 1]);
                }

                d[i][j] = matrix.array[i][j] + list.stream().min(Comparator.<Double>naturalOrder()).get();
                count[i][j] = 1 + listCount.stream().min(Comparator.<Double>naturalOrder()).get();
            }
        }

        return d[matrix.array[0].length - 1][matrix.array[1].length - 1] / count[matrix.array[0].length - 1][matrix.array[1].length - 1];
    }
}
