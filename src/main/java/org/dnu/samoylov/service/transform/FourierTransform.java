package org.dnu.samoylov.service.transform;

import org.dnu.samoylov.service.Sound;

public class FourierTransform {
    private Sound sound;

    public FourierTransform(Sound sound) {
        this.sound = sound;
        this.sound.laneRepresentationFourier = new double[sound.countOfLines] [ 256 / 2];
    }

    public double calculateA(int k, int l) {
        double a = 0;
        int N = 256;
        for (int i = l * N; i < l * N + N + 1; i++) {
            a += sound.normalizationArr.get(i) * Math.cos(2 * Math.PI * k * i / N);
        }
        return a * (2.0 / (N));
    }

    public double calculateB(int k, int l) {
        double b = 0;
        int N = 256;
        for (int i = l * N; i < l * N + N + 1; i++) {
            b += sound.normalizationArr.get(i) * Math.sin(2 * Math.PI * k * i / N);
        }
        return b * (2.0 / (N));
    }

    public double calculateC(int k, int l) {
        double A = calculateA(k, l);
        double B = calculateB(k, l);
        return Math.pow(A, 2) + Math.pow(B, 2);
    }

    public void initFourierArray() {
        for (int i = 0; i < sound.countOfLines; i++) {
            for (int j = 0; j < 256 / 2; j++) {
                sound.laneRepresentationFourier[i][j] = Math.sqrt(calculateC(j, i));
            }
        }

    }

}
