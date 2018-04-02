package com.robertboothby.codegen;

import java.util.function.Function;

/**
 * This class holds the outcome of the generation.
 */
public class FunctionResult<R> {
    private final String resultIdentifier;
    private final Exception exception;
    private final R result;

    public FunctionResult(String resultIdentifier, R result) {
        this.resultIdentifier = resultIdentifier;
        this.result = result;
        exception = null;
    }

    public FunctionResult(String resultIdentifier, Exception exception) {
        this.resultIdentifier = resultIdentifier;
        this.exception = exception;
        this.result = null;
    }

    public boolean isSuccessful() {
        return result != null;
    }

    public boolean isFailure() {
        return exception != null;
    }

    public String getResultIdentifier() {
        return resultIdentifier;
    }

    public Exception getException() {
        return exception;
    }

    public R getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "FunctionResult{" +
                "resultIdentifier='" + resultIdentifier + '\'' +
                ", exception=" + Utilities.exceptionAsString(exception) +
                ", result='" + result + '\'' +
                '}';
    }


}
