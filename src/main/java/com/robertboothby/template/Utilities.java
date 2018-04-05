package com.robertboothby.template;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Function;

public class Utilities {
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

    public static <T, R, E extends Exception> Function<T, FunctionResult<R>> wrap(FunctionWithException<T,R,E> function){
        return type -> {
            try {
                return new FunctionResult<>(type.toString(), function.apply(type));
            } catch (Exception e) {
                return new FunctionResult<>(type.toString(), e);
            }
        };
    }
}
