package org.dnu.samoylov.service.transform;

import org.dnu.samoylov.service.Sound;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WalschTransform {
    private Sound sound;
    int[][] adamarArray=new int[256][256];

    public WalschTransform(Sound sound) {
        this.sound = sound;
        this.sound.laneRepresentationWalsch = new double[sound.countOfLines][256];
    }


    public void initWalschArray() {
        initAdamarArray();

        for (int i = 0; i < sound.countOfLines; i++) {
            for (int j = 0; j < 256; j++) {
                sound.laneRepresentationWalsch[i][j]=Math.abs(calculateC(j, i));
            }
        }
    }


    private double calculateC(int k, int l) {
        double c = 0;
        int N = 256;
        for (int i = l * N; i < l * N + N; i++) {
            c += sound.normalizationArr.get(i) * adamarArray[k][i - l * N];
        }
        c /= N;
        return c;
    }



    private void initAdamarArray() {
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                adamarArray[i][j]= getAdamarElement(i, j);
            }
        }
    }


    private int getAdamarElement(int h, int x) {
        Integer[] hBinaryArray = convertIntToBinaryArray(h);
        Integer[] xBinaryArray = convertIntToBinaryArray(x);
        int sum = 0;
        for (int i = 0; i < hBinaryArray.length; i++) {
            sum += hBinaryArray[i] & xBinaryArray[i];
        }
        return (sum % 2 == 0) ? 1 : -1;
    }

    private Integer[] convertIntToBinaryArray(int val) {
        Integer[] bits = new Integer[32];
        for (int i = 0; i < 32; i++) {
            bits[i] = getBit(val, i);
        }

        List<Integer> bitsList = Arrays.asList(bits);
        Collections.reverse(bitsList);
        return (Integer[]) bitsList.toArray();
    }

    private int getBit(int n, int k) {
        return (n >> k) & 1;
    }


}
