package org.dnu.samoylov.service;

import org.dnu.samoylov.service.transform.FourierTransform;
import org.dnu.samoylov.service.transform.WalschTransform;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OpenHelper {
    static public Sound open(byte[] byte_arr, double f) {

        Sound sound = new Sound();
        sound.arr = new ArrayList<>();

        byteToLong(byte_arr, sound.arr, Sound.HEADER_LENGTH);

        NormalizationAndLatentPeriodsRemover.removeLatentPeriods(sound);
        NormalizationAndLatentPeriodsRemover.normalization(sound);
        sound.countOfLines = sound.normalizationArr.size() / 256;

        FourierTransform fourier = new FourierTransform(sound);
        fourier.initFourierArray();

        WalschTransform walsch = new WalschTransform(sound);
        walsch.initWalschArray();

        LineRepresentation representation = new LineRepresentation(sound, f);
        representation.initRepresentationFourier();
        representation.initRepresentationWalsch();
        representation.initRepresentationFourierSmoothing();
        representation.initRepresentationWalschSmoothing();

        return sound;
    }

    static void byteToLong(byte[] byte_arr, List<Short> long_arr, int header_length) {

        for (int i = header_length / 2; i < byte_arr.length / 2; i+=1) {
            short e = (short) ((byte_arr[i * 2] & 0xff) | (byte_arr[i * 2 + 1] << 8));
            long_arr.add(e);
        }
    }


    public static void saveToFileArray(Sound sound, String method, String nameOfFile) {
        List<String> linesToWrite = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            StringBuilder line = new StringBuilder();
            for (int colIndex = 0; colIndex < sound.countOfLines; colIndex++) {
                if (Objects.equals(method, "fourer")) {
                    line.append(sound.lineRepresentationFourierSmoothing[rowIndex][colIndex]).append(" ");
                }
                if (Objects.equals(method, "walsch")) {
                    line.append(sound.lineRepresentationWalschSmoothing[rowIndex][colIndex]).append(" ");
                }
            }

            linesToWrite.add(line.toString());
        }

        try (PrintWriter out = new PrintWriter(nameOfFile.substring(0, nameOfFile.length() - 4) + ".dat")) {
            for (String s : linesToWrite) {
                out.println(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}