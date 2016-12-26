package org.dnu.samoylov.util.onetimerun;

public interface RunLambda<RESULT> {
    RESULT runMethod();

    default OneTimeRunMethod<RESULT> method() {
        return new OneTimeRunMethod<>(this);
    }
}
