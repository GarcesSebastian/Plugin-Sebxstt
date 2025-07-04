package io.papermc.sebxstt.interfaces;

public interface Resolver<T, R> {
    R resolve(T data);
}
