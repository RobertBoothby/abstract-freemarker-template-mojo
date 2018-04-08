package com.robertboothby.utilities.lambda;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LambdaUtilities {

    public static String exceptionAsString(Exception exception) {
        try (StringWriter writer = new StringWriter()) {
            if(exception == null) {
                exception = new IllegalArgumentException("No Exception received to turn into String");
            }
            exception.printStackTrace(new PrintWriter(writer));
            return writer.append("\n}").toString();
        } catch (IOException e) {
            //Should never happen but need to capture exception if it does...
            throw new RuntimeException(e);
        }
    }

    public static <R, E extends Exception> FunctionResult<R> wrap(String identifier, SupplierWithException<R, E> supplier){
        try {
            return new FunctionResult<>(identifier, supplier.get());
        } catch (Exception e) {
            return new FunctionResult<>(identifier, e);
        }
    }

    public static <T, R, E extends Exception> Function<T, FunctionResult<R>> wrap(FunctionWithException<T,R,E> function){
        return parameter -> wrap( parameter.toString(), () -> function.apply(parameter));
    }


    public static <T, U, R, E extends Exception> BiFunction<T, U, FunctionResult<R>> wrap(BiFunctionWithException<T,U,R,E> function){
        return (parameter1, parameter2) -> wrap(parameter1.toString(), () -> function.apply(parameter1, parameter2));
    }

}
