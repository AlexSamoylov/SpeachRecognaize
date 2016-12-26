package org.dnu.samoylov.service;

import java.util.List;

public class Sound {

        public static final int HEADER_LENGTH = 44;


        public List<Short> arr;
        public List<Short> arrWhithoutLatentPeriods;
        public List<Double> normalizationArr;
        public double[][] laneRepresentationFourier;
        public double[][] laneRepresentationWalsch;

        public double[][] lineRepresentationFourier;
        public double[][] lineRepresentationWalsch;

        public double[][] lineRepresentationFourierSmoothing;
        public double[][] lineRepresentationWalschSmoothing;

        public List<Double> lineRepresentationInSegment;
        public List<Double> verificationSegmentation;
        public int countOfLines;
}
