package com.robertboothby.utilities.lambda;

import java.util.Optional;

/**
 * This class holds the outcome of any function that may generate an exception.
 * @param <R> the type of the result.
 */
public class FunctionResult<R> {
    private final String resultIdentifier;
    private final Exception exception;
    private final R result;

    /**
     * Constructor for an outcome that does not result in an exception.
     * @param resultIdentifier An identifier for the result, used mainly in a streaming context where there may be
     *                         multiple results.
     * @param result The result.
     */
    public FunctionResult(String resultIdentifier, R result) {
        this.resultIdentifier = resultIdentifier;
        this.result = result;
        exception = null;
    }

    /**
     * Constructor for an outcome that does result in an exception.
     * @param resultIdentifier An identifier for the result, used mainly in a streaming context where there may be
     *                         multiple results.
     * @param exception the exception arising as a result of the function.
     */
    public FunctionResult(String resultIdentifier, Exception exception) {
        this.resultIdentifier = resultIdentifier;
        this.exception = exception;
        this.result = null;
    }

    /**
     * Determine if this is a normal result with no exception.
     * @return true if there is no exception.
     */
    public boolean isNormal() {
        return result != null;
    }

    /**
     * Determine if this is an exceptional result.
     * @return true if there is an exception.
     */
    public boolean isExceptional() {
        return exception != null;
    }

    /**
     * Get the identifier for the result, particularly useful if there is more than one result and you may need to
     * identify which result we are dealing with.
     * @return the result identifier.
     */
    public String getResultIdentifier() {
        return resultIdentifier;
    }

    /**
     * Get the exception associated with this result if it is present.
     * @return the exception if present.
     */
    public Optional<Exception> getException() {
        return Optional.ofNullable(exception);
    }

    /**
     * Get the result of the function if it is present.
     * @return the result of the function.
     */
    public Optional<R> getResult() {
        return Optional.ofNullable(result);
    }

    @Override
    public String toString() {
        return "FunctionResult{" +
                "resultIdentifier='" + resultIdentifier + '\'' +
                ", exception=" + LambdaUtilities.exceptionAsString(exception) +
                ", result='" + result + '\'' +
                '}';
    }


}
