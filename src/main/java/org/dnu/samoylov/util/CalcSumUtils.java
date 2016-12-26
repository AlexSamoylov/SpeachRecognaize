package org.dnu.samoylov.util;

import java.util.Collection;

public class CalcSumUtils {

    public static <ELEMENT> Double calculateSum(Collection<ELEMENT> collection,
                                         ElementAlgorithm<ELEMENT, Double> elementAlgorithm) {
        double result = 0;
        for (ELEMENT element : collection) {
            result += elementAlgorithm.run(element);
        }

        return result;
    }

    public static <ELEMENT> Double calculateMeanSum(Collection<ELEMENT> collection,
                                             ElementAlgorithm<ELEMENT, Double> elementAlgorithm) {
        double result = calculateSum(collection, elementAlgorithm);

        return result / collection.size();
    }


    public static interface ElementAlgorithm<ELEMENT, RESULT> {
        public RESULT run(ELEMENT element);
    }
}
