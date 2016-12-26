package org.dnu.samoylov.util.onetimerun;


public class OneTimeRunMethod<RESULT> {
    private final RunLambda<RESULT> runLambda;

    RESULT result;

    protected OneTimeRunMethod(RunLambda<RESULT> runLambda) {
        this.runLambda = runLambda;
    }

    public RESULT get() {
        if (result==null) {
            result = runLambda.runMethod();
        }
        return result;
    }

}
