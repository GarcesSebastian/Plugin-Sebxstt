package io.papermc.sebxstt.instances.interfaces;

public interface Resolver<T, R> {
    R resolve(T data);
}
