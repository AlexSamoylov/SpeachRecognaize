package org.dnu.samoylov.service.dinamic;

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



    public void calculateAllMatrixDistance() {
        for (int i = 0; i < fileMatrix.size(); i++) {
            distanceMatrixes.add(calculacteDistanceMatrix(currentMatrix, fileMatrix.get(i)));
        }
    }

    public void initDistances() {
        for (int i = 0; i < distanceMatrixes.size(); i++) {
            distances.add(calculateMinDistance(distanceMatrixes.get(i)));
        }
    }


    private DistanceMatrix calculacteDistanceMatrix(FourerMatrix firstMatrix, FourerMatrix secondMatrix) {
        Double[][] distanceMatrix = new Double[firstMatrix.array[0].length][secondMatrix.array[0].length];

        for (int i = 0; i < firstMatrix.array[0].length; i++) {
            for (int j = 0; j < secondMatrix.array[0].length; j++) {
                distanceMatrix[i][j] = calculateEuclideanMetric(getColum(firstMatrix.array, i), getColum(secondMatrix.array, j));
            }
        }
        return new DistanceMatrix(distanceMatrix);
    }



    private double calculateEuclideanMetric(Double[] firstArray, Double[] secondArray) {
        double distance = 0;

        for (int i = 0; i < firstArray.length; i++) {
            distance += Math.pow(secondArray[i] - firstArray[i], 2);
        }
        distance = Math.sqrt(distance);

        return distance;
    }

    private Double[] getColum(double[][] array, int columnNumber) {
        List<Double> listElem = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            listElem.add(array[i][columnNumber]);
        }
        return listElem.toArray(new Double[listElem.size()]);
    }



    private double calculateMinDistance(DistanceMatrix matrix) {
        int firstLevelSize = matrix.array.length;
        int secondLevelSize = matrix.array[0].length;

        double[][] d = new double[firstLevelSize][secondLevelSize];
        double[][] count = new double[firstLevelSize][secondLevelSize];

        for (int i = 0; i < firstLevelSize; i++) {
            for (int j = 0; j < secondLevelSize; j++) {

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

        return d[matrix.array.length - 1][matrix.array[1].length - 1] / count[matrix.array.length - 1][matrix.array[1].length - 1];
    }
}
