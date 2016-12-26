package org.dnu.samoylov.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NormalizationAndLatentPeriodsRemover {

    public static void removeLatentPeriods(Sound sound) {
        double sqrtVarience = Math.sqrt(calcVariance(sound.arr));
        double coef = 1.0 / 3.0;
        boolean isFinish = false;
        sound.arrWhithoutLatentPeriods = new ArrayList<>();

        sound.arrWhithoutLatentPeriods.addAll(
                sound.arr.stream().collect(Collectors.toList())
        );

        for (int i = 0; i < sound.arrWhithoutLatentPeriods.size(); i++) {
            if (!isFinish) {
                if (sound.arrWhithoutLatentPeriods.get(0) >= coef * sqrtVarience || sound.arrWhithoutLatentPeriods.get(0) <= -coef * sqrtVarience) {
                    isFinish = true;
                } else {
                    sound.arrWhithoutLatentPeriods.remove(0);
                }
            }
        }
        isFinish = false;
        for (int i = sound.arrWhithoutLatentPeriods.size() - 1; i >= 0; i--) {
            if (!isFinish) {
                if (sound.arrWhithoutLatentPeriods.get(sound.arrWhithoutLatentPeriods.size() - 1) >= coef * sqrtVarience
                        || sound.arrWhithoutLatentPeriods.get(sound.arrWhithoutLatentPeriods.size() - 1) <= -coef * sqrtVarience) {
                    isFinish = true;
                } else {
                    sound.arrWhithoutLatentPeriods.remove(sound.arrWhithoutLatentPeriods.size() - 1);
                }
            }
        }
    }

    public static void normalization(Sound sound) {
        double varience = calcVariance(sound.arrWhithoutLatentPeriods);
        sound.normalizationArr = new ArrayList<>();

        sound.normalizationArr = sound.arrWhithoutLatentPeriods.stream().map(x -> (double) x / Math.sqrt(varience)).collect(Collectors.toList());
    }



    public static double calcVariance(List<Short> arr) {
        double varience = 0;
        for (Short anArr : arr) {
            varience += Math.pow(anArr, 2);
        }
        varience /= arr.size();
        return varience;
    }
}