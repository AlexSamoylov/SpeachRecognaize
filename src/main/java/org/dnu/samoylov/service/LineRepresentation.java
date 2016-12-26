package org.dnu.samoylov.service;

import java.util.ArrayList;
import java.util.List;

public class LineRepresentation {
    public static final int N = 7;
    public static final int lineCount = 9;

    Sound sound;
    double f;
    List<Double> c;

    public LineRepresentation(Sound sound, double f) {
        this.sound = sound;

        this.sound.lineRepresentationFourier = new double[lineCount][sound.countOfLines];
        this.sound.lineRepresentationWalsch = new double[lineCount][sound.countOfLines];

        this.sound.lineRepresentationFourierSmoothing = new double[lineCount][sound.countOfLines];
        this.sound.lineRepresentationWalschSmoothing = new double[lineCount][sound.countOfLines];

        this.f = f;
        this.c = new ArrayList<>();
    }

    public void initRepresentationWalsch() {
        for (int i = 0; i < sound.countOfLines; i++) {
            sound.lineRepresentationWalsch[0][i] = calculateSum(0, 3, i, sound.laneRepresentationWalsch);
            sound.lineRepresentationWalsch[1][i] = calculateSum(4, 7, i, sound.laneRepresentationWalsch);
            sound.lineRepresentationWalsch[2][i] = calculateSum(8, 11, i, sound.laneRepresentationWalsch);
            sound.lineRepresentationWalsch[3][i] = calculateSum(12, 15, i, sound.laneRepresentationWalsch);
            sound.lineRepresentationWalsch[4][i] = calculateSum(16, 19, i, sound.laneRepresentationWalsch);
            sound.lineRepresentationWalsch[5][i] = calculateSum(20, 29, i, sound.laneRepresentationWalsch);
            sound.lineRepresentationWalsch[6][i] = calculateSum(30, 49, i, sound.laneRepresentationWalsch);
            sound.lineRepresentationWalsch[7][i] = calculateSum(50, 99, i, sound.laneRepresentationWalsch);
            sound.lineRepresentationWalsch[8][i] = calculateSum(100, 255, i, sound.laneRepresentationWalsch);
        }

    }

    public void initRepresentationFourier() {
        for (int i = 0; i < sound.countOfLines; i++) {
            sound.lineRepresentationFourier[0][i] = calculateSum(0, 1, i, sound.laneRepresentationFourier);
            sound.lineRepresentationFourier[1][i] = calculateSum(2, 3, i, sound.laneRepresentationFourier);
            sound.lineRepresentationFourier[2][i] = calculateSum(4, 5, i, sound.laneRepresentationFourier);
            sound.lineRepresentationFourier[3][i] = calculateSum(6, 7, i, sound.laneRepresentationFourier);
            sound.lineRepresentationFourier[4][i] = calculateSum(8, 9, i, sound.laneRepresentationFourier);
            sound.lineRepresentationFourier[5][i] = calculateSum(10, 14, i, sound.laneRepresentationFourier);
            sound.lineRepresentationFourier[6][i] = calculateSum(15, 24, i, sound.laneRepresentationFourier);
            sound.lineRepresentationFourier[7][i] = calculateSum(25, 49, i, sound.laneRepresentationFourier);
            sound.lineRepresentationFourier[8][i] = calculateSum(50, 127, i, sound.laneRepresentationFourier);
        }
    }


    public void initRepresentationFourierSmoothing() {
        for (int i = 0; i < lineCount; i++) {
            for (int j = 0; j < sound.countOfLines; j++) {
                sound.lineRepresentationFourierSmoothing[i][j] = calculateYSmoothing(j, i, sound.lineRepresentationFourier, f);
            }
        }
    }

    public void initRepresentationWalschSmoothing() {
        for (int i = 0; i < lineCount; i++) {
            for (int j = 0; j < sound.countOfLines; j++) {
                sound.lineRepresentationWalschSmoothing[i][j] = calculateYSmoothing(j, i, sound.lineRepresentationWalsch, f);
            }
        }
    }


    private double calculateSum(int from, int to, int line, double[][] arr) {
        double sum = 0;
        for (int j = from; j <= to; j++) {
            sum += Math.pow(arr[line][j], 2);
        }
        return Math.sqrt(sum);
    }


    private double calculateYSmoothing(int l, int line, double[][] arr, double f) {
        initCArray(f);

        double y = 0;
        for (int i = -N; i <= N; i++) {
            int index = l - i;
            if (l - i >= sound.countOfLines) {
                index = -i - 1;
            }
            if (l - i < 0) {
                index = sound.countOfLines - i;
            }
            y += c.get(N + i) * arr[line][index];
        }
        return y;
    }


    private void initCArray(double f) {
        c.clear();
        for (int k = -N; k <= N; k++) {
            if (k == 0) {
                c.add(2 * f);
            } else {
                c.add(calculateC(k, f));
            }
        }
    }

    private double calculateC(int k, double f) {
        return (1.0 / (k * Math.PI)) * Math.sin(2.0 * Math.PI * k * f);
    }

}
