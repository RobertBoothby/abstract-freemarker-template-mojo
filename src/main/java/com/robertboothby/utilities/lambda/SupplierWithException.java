package com.robertboothby.utilities.lambda;

@FunctionalInterface
public interface SupplierWithException<T, E extends Exception> {

    public T get() throws E;
}
